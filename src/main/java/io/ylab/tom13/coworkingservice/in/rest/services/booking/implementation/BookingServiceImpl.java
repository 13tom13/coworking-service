package io.ylab.tom13.coworkingservice.in.rest.services.booking.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.BookingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.booking.BookingService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.BookingMapper;

import java.time.LocalDate;
import java.util.*;

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;


    public BookingServiceImpl() {
        this.bookingRepository = BookingRepositoryCollection.getInstance();
    }

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) throws BookingConflictException, RepositoryException {
        if (bookingDTO.date().isBefore(LocalDate.now())) {
            throw new BookingConflictException("Время бронирования не может быть в прошлом");
        }
        Booking newBooking = new Booking(0, bookingDTO.userId(), bookingDTO.coworkingId(), bookingDTO.date(), bookingDTO.timeSlots());
        Optional<Booking> bookingFromRepository = bookingRepository.createBooking(newBooking);
        if (bookingFromRepository.isEmpty()) {
            throw new RepositoryException("Не удалось создать бронирование");
        }
        return bookingMapper.toBookingDTO(bookingFromRepository.get());
    }

    @Override
    public void cancelBooking(long bookingId) throws BookingNotFoundException {
        bookingRepository.deleteBooking(bookingId);
    }

    @Override
    public List<BookingDTO> getBookingsByUser(long userId) throws BookingNotFoundException {
        Collection<Booking> bookingsByUser = bookingRepository.getBookingsByUser(userId);
        return bookingsByUser.stream().map(bookingMapper::toBookingDTO).toList();
    }

    @Override
    public List<BookingDTO> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException {
        Collection<Booking> bookingsByUserAndDate = bookingRepository.getBookingsByUserAndDate(userId, date);
        return bookingsByUserAndDate.stream().map(bookingMapper::toBookingDTO).toList();
    }

    @Override
    public List<BookingDTO> getBookingsByUserAndCoworking(long userId, long coworkingId) throws BookingNotFoundException {
        Collection<Booking> bookingsByUserAndCoworking = bookingRepository.getBookingsByUserAndCoworking(userId, coworkingId);
        return bookingsByUserAndCoworking.stream().map(bookingMapper::toBookingDTO).toList();
    }


    @Override
    public List<TimeSlot> getAvailableSlots(long coworkingId, LocalDate date) {
        List<Booking> bookings = new ArrayList<>(bookingRepository.getBookingsByCoworkingAndDate(coworkingId, date));
        List<TimeSlot> availableSlots = new ArrayList<>(Arrays.asList(TimeSlot.values()));
        if (bookings.isEmpty()) {
            return availableSlots;
        }
        for (Booking booking : bookings) {
            for (TimeSlot bookedSlot : booking.timeSlots()) {
                availableSlots.remove(bookedSlot);
            }
        }
        return availableSlots;
    }

    @Override
    public BookingDTO getBookingById(long bookingId) throws BookingNotFoundException {
        Optional<Booking> bookingById = bookingRepository.getBookingById(bookingId);
        if (bookingById.isEmpty())  {
            throw new BookingNotFoundException("Бронирование с таким id не найдено");
        }
        return bookingMapper.toBookingDTO(bookingById.get());
    }

    @Override
    public BookingDTO updateBooking(BookingDTO booking) throws BookingNotFoundException, BookingConflictException, RepositoryException {
        if (booking.date().isBefore(LocalDate.now())) {
            throw new BookingConflictException("Время бронирования не может быть в прошлом");
        }

        Booking updateBooking = bookingMapper.toBooking(booking);
        Optional<Booking> bookingFromRep = bookingRepository.updateBooking(updateBooking);
        if (bookingFromRep.isEmpty())  {
            throw new RepositoryException("Не удалось обновить бронирование");
        }

        return bookingMapper.toBookingDTO(bookingFromRep.get());
    }

}

