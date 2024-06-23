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
    public ResponseDTO<List<BookingDTO>> getBookingsByUserAndDate(long userId, LocalDate date) {
        try {
            List<BookingDTO> bookingsByUserAndDate = bookingService.getBookingsByUserAndDate(userId, date);
            return ResponseDTO.success(bookingsByUserAndDate);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<List<BookingDTO>> getBookingsByUserAndCoworking(long userId, long coworkingId) {
        try {
            List<BookingDTO> bookingsByUserAndCoworking = bookingService.getBookingsByUserAndCoworking(userId, coworkingId);
            return ResponseDTO.success(bookingsByUserAndCoworking);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }


    @Override
    public ResponseDTO<List<TimeSlot>> getAvailableSlots(long coworkingId, LocalDate date) {
        List<TimeSlot> bookingsByCoworking = bookingService.getAvailableSlots(coworkingId, date);
        return ResponseDTO.success(bookingsByCoworking);
    }

    @Override
    public ResponseDTO<BookingDTO> getBookingById(long bookingId) {
        try {
            BookingDTO bookingsById = bookingService.getBookingById(bookingId);
            return ResponseDTO.success(bookingsById);
        } catch (BookingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<BookingDTO> updateBooking(BookingDTO booking) {
        try {
            System.out.println(booking.id());
            BookingDTO updatedBooking = bookingService.updateBooking(booking);
            return ResponseDTO.success(updatedBooking);
        } catch (BookingNotFoundException | BookingConflictException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}

