package io.ylab.tom13.coworkingservice.out.rest.services.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.out.entity.model.Booking;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.out.utils.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Реализация интерфейса {@link BookingService}.
 * Сервиса для управления операциями бронирования.
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    /**
     * {@inheritDoc}
     */
    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        if (bookingDTO.date().isBefore(LocalDate.now())) {
            throw new BookingConflictException("Время бронирования не может быть в прошлом");
        }
        Booking newBooking = bookingMapper.toBooking(bookingDTO);
        Optional<Booking> bookingFromRepository = bookingRepository.createBooking(newBooking);

        if (bookingFromRepository.isEmpty()) {
            throw new RepositoryException("Не удалось создать бронирование");
        }
        return bookingMapper.toBookingDTO(bookingFromRepository.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelBooking(long bookingId) {
        bookingRepository.deleteBooking(bookingId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookingDTO> getBookingsByUser(long userId) {
        Collection<Booking> bookingsByUser = bookingRepository.getBookingsByUser(userId);
        return bookingsByUser.stream().map(bookingMapper::toBookingDTO).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookingDTO> getBookingsByUserAndDate(long userId, LocalDate date) {
        Collection<Booking> bookingsByUserAndDate = bookingRepository.getBookingsByUserAndDate(userId, date);
        return bookingsByUserAndDate.stream().map(bookingMapper::toBookingDTO).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookingDTO> getBookingsByUserAndCoworking(long userId, long coworkingId) {
        Collection<Booking> bookingsByUserAndCoworking = bookingRepository.getBookingsByUserAndCoworking(userId, coworkingId);
        return bookingsByUserAndCoworking.stream().map(bookingMapper::toBookingDTO).toList();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public BookingDTO getBookingById(long bookingId) {
        Optional<Booking> bookingById = bookingRepository.getBookingById(bookingId);
        if (bookingById.isEmpty()) {
            throw new BookingNotFoundException("Бронирование с таким id не найдено");
        }
        return bookingMapper.toBookingDTO(bookingById.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BookingDTO updateBooking(BookingDTO booking) {
        if (booking.date().isBefore(LocalDate.now())) {
            throw new BookingConflictException("Время бронирования не может быть в прошлом");
        }

        Booking updateBooking = bookingMapper.toBooking(booking);
        Optional<Booking> bookingFromRep = bookingRepository.updateBooking(updateBooking);
        if (bookingFromRep.isEmpty()) {
            throw new RepositoryException("Не удалось обновить бронирование");
        }

        return bookingMapper.toBookingDTO(bookingFromRep.get());
    }

}

