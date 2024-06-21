package io.ylab.tom13.coworkingservice.in.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.controller.BookingController;
import io.ylab.tom13.coworkingservice.in.rest.services.BookingService;

import java.time.LocalDateTime;
import java.util.Collection;

public class BookingControllerImpl implements BookingController {

    private final BookingService bookingService;

    public BookingControllerImpl() {
        this.bookingService = null;
    }

    @Override
    public ResponseDTO<Booking> createBooking(BookingDTO bookingDTO) {
        try {
            Booking booking = bookingService.createBooking(bookingDTO);
            return ResponseDTO.success(booking);
        } catch (BookingConflictException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<Void> cancelBooking(long bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseDTO.success(null);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<Collection<Booking>> getAllBookings() {
        return ResponseDTO.success(bookingService.getAllBookings());
    }

    @Override
    public ResponseDTO<Collection<Booking>> getBookingsByUser(long userId) {
        return ResponseDTO.success(bookingService.getBookingsByUser(userId));
    }

    @Override
    public ResponseDTO<Collection<Booking>> getBookingsBySpace(long spaceId) {
        return ResponseDTO.success(bookingService.getBookingsBySpace(spaceId));
    }

    @Override
    public ResponseDTO<Collection<Booking>> getBookingsByDate(LocalDateTime date) {
        return ResponseDTO.success(bookingService.getBookingsByDate(date));
    }
}

