package io.ylab.tom13.coworkingservice.in.rest.services.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.BookingRepositoryJdbc;
import io.ylab.tom13.coworkingservice.in.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.BookingMapper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static io.ylab.tom13.coworkingservice.in.database.DatabaseConnection.getConnection;

/**
 * Реализация интерфейса {@link BookingService}.
 * Сервиса для управления операциями бронирования.
 */
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    /**
     * Конструктор для инициализации сервиса бронирования.
     */
    public BookingServiceImpl() {
        try {
            this.bookingRepository = new BookingRepositoryJdbc(getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelBooking(long bookingId) throws BookingNotFoundException, RepositoryException {
        bookingRepository.deleteBooking(bookingId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookingDTO> getBookingsByUser(long userId) throws BookingNotFoundException, RepositoryException {
        Collection<Booking> bookingsByUser = bookingRepository.getBookingsByUser(userId);
        return bookingsByUser.stream().map(bookingMapper::toBookingDTO).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookingDTO> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException, RepositoryException {
        Collection<Booking> bookingsByUserAndDate = bookingRepository.getBookingsByUserAndDate(userId, date);
        return bookingsByUserAndDate.stream().map(bookingMapper::toBookingDTO).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookingDTO> getBookingsByUserAndCoworking(long userId, long coworkingId) throws BookingNotFoundException, RepositoryException {
        Collection<Booking> bookingsByUserAndCoworking = bookingRepository.getBookingsByUserAndCoworking(userId, coworkingId);
        return bookingsByUserAndCoworking.stream().map(bookingMapper::toBookingDTO).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimeSlot> getAvailableSlots(long coworkingId, LocalDate date) throws RepositoryException {
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
    public BookingDTO getBookingById(long bookingId) throws BookingNotFoundException, RepositoryException {
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
    public BookingDTO updateBooking(BookingDTO booking) throws BookingNotFoundException, BookingConflictException, RepositoryException {
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

