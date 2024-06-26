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
import io.ylab.tom13.coworkingservice.in.utils.SecurityController;

import java.time.LocalDate;
import java.util.List;

public class BookingControllerImpl extends SecurityController implements BookingController {

    private final BookingService bookingService;

    public BookingControllerImpl() {
        this.bookingService = new BookingServiceImpl();
    }

    @Override
    public ResponseDTO<BookingDTO> createBooking(BookingDTO bookingDTO) {
        if (!hasAuthenticated()){
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            BookingDTO booking = bookingService.createBooking(bookingDTO);
            return ResponseDTO.success(booking);
        } catch (BookingConflictException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<Void> cancelBooking(long bookingId) {
        if (!hasAuthenticated()){
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseDTO.success(null);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }


    @Override
    public ResponseDTO<List<BookingDTO>> getBookingsByUser(long userId) {
        if (!hasAuthenticated()){
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            List<BookingDTO> bookingsByUser = bookingService.getBookingsByUser(userId);
            return ResponseDTO.success(bookingsByUser);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<List<BookingDTO>> getBookingsByUserAndDate(long userId, LocalDate date) {
        if (!hasAuthenticated()){
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            List<BookingDTO> bookingsByUserAndDate = bookingService.getBookingsByUserAndDate(userId, date);
            return ResponseDTO.success(bookingsByUserAndDate);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<List<BookingDTO>> getBookingsByUserAndCoworking(long userId, long coworkingId) {
        if (!hasAuthenticated()){
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            List<BookingDTO> bookingsByUserAndCoworking = bookingService.getBookingsByUserAndCoworking(userId, coworkingId);
            return ResponseDTO.success(bookingsByUserAndCoworking);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }


    @Override
    public ResponseDTO<List<TimeSlot>> getAvailableSlots(long coworkingId, LocalDate date) {
        if (!hasAuthenticated()){
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        List<TimeSlot> bookingsByCoworking = bookingService.getAvailableSlots(coworkingId, date);
        return ResponseDTO.success(bookingsByCoworking);
    }

    @Override
    public ResponseDTO<BookingDTO> getBookingById(long bookingId) {
        if (!hasAuthenticated()){
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            BookingDTO bookingsById = bookingService.getBookingById(bookingId);
            return ResponseDTO.success(bookingsById);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<BookingDTO> updateBooking(BookingDTO booking) {
        if (!hasAuthenticated()){
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

