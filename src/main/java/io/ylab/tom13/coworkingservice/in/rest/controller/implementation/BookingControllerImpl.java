package io.ylab.tom13.coworkingservice.in.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.controller.BookingController;
import io.ylab.tom13.coworkingservice.in.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.BookingServiceImpl;
import io.ylab.tom13.coworkingservice.in.security.SecurityController;

import java.time.LocalDate;
import java.util.List;

/**
 * Реализация интерфейса {@link BookingController}.
 * Обрабатывает запросы, связанные с бронированием в системе.
 */
public class BookingControllerImpl extends SecurityController implements BookingController {

    private final BookingService bookingService;

    /**
     * Конструктор для инициализации объекта контроллера бронирования.
     */
    public BookingControllerImpl() {
        this.bookingService = new BookingServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<BookingDTO> createBooking(BookingDTO bookingDTO) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            BookingDTO booking = bookingService.createBooking(bookingDTO);
            return ResponseDTO.success(booking);
        } catch (BookingConflictException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<Void> cancelBooking(long bookingId) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseDTO.success(null);
        } catch (BookingNotFoundException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<List<BookingDTO>> getBookingsByUser(long userId) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            List<BookingDTO> bookingsByUser = bookingService.getBookingsByUser(userId);
            return ResponseDTO.success(bookingsByUser);
        } catch (BookingNotFoundException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<List<BookingDTO>> getBookingsByUserAndDate(long userId, LocalDate date) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            List<BookingDTO> bookingsByUserAndDate = bookingService.getBookingsByUserAndDate(userId, date);
            return ResponseDTO.success(bookingsByUserAndDate);
        } catch (BookingNotFoundException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<List<BookingDTO>> getBookingsByUserAndCoworking(long userId, long coworkingId) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            List<BookingDTO> bookingsByUserAndCoworking = bookingService.getBookingsByUserAndCoworking(userId, coworkingId);
            return ResponseDTO.success(bookingsByUserAndCoworking);
        } catch (BookingNotFoundException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<List<TimeSlot>> getAvailableSlots(long coworkingId, LocalDate date) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            List<TimeSlot> availableSlots = availableSlots = bookingService.getAvailableSlots(coworkingId, date);
            return ResponseDTO.success(availableSlots);
        } catch (RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<BookingDTO> getBookingById(long bookingId) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            BookingDTO bookingById = bookingService.getBookingById(bookingId);
            return ResponseDTO.success(bookingById);
        } catch (BookingNotFoundException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<BookingDTO> updateBooking(BookingDTO booking) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            BookingDTO updatedBooking = bookingService.updateBooking(booking);
            return ResponseDTO.success(updatedBooking);
        } catch (BookingNotFoundException | BookingConflictException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}

