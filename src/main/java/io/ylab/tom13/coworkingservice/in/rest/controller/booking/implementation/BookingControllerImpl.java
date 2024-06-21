package io.ylab.tom13.coworkingservice.in.rest.controller.booking.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.controller.booking.BookingController;
import io.ylab.tom13.coworkingservice.in.rest.services.booking.BookingService;
import io.ylab.tom13.coworkingservice.in.rest.services.booking.implementation.BookingServiceImpl;

import java.time.LocalDate;
import java.util.List;

public class BookingControllerImpl implements BookingController {

    private final BookingService bookingService;

    public BookingControllerImpl() {
        this.bookingService = new BookingServiceImpl();
    }

    @Override
    public ResponseDTO<BookingDTO> createBooking(BookingDTO bookingDTO) {
        try {
            BookingDTO booking = bookingService.createBooking(bookingDTO);
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
    public ResponseDTO<List<BookingDTO>> getBookingsByUser(long userId) {
        try {
            List<BookingDTO> bookingsByUser = bookingService.getBookingsByUser(userId);
            return ResponseDTO.success(bookingsByUser);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<List<BookingDTO>> getBookingsByCoworking(long coworkingId) {
        try {
            List<BookingDTO> bookingsByCoworking = bookingService.getBookingsByCoworking(coworkingId);
            return ResponseDTO.success(bookingsByCoworking);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<List<TimeSlot>> getAvailableSlots(long coworkingId, LocalDate date) {
        List<TimeSlot> bookingsByCoworking = bookingService.getAvailableSlots(coworkingId, date);
        return ResponseDTO.success(bookingsByCoworking);
    }
}

