package io.ylab.tom13.coworkingservice.in.rest.services.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDTO createBooking(BookingDTO bookingDTO) throws BookingConflictException;

    void cancelBooking(long bookingId) throws BookingNotFoundException;

    List<BookingDTO> getBookingsByUser(long userId) throws BookingNotFoundException;

    List<BookingDTO> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException;

    List<TimeSlot> getAvailableSlots(long coworkingId, LocalDate date);

    BookingDTO getBookingById(long bookingId) throws BookingNotFoundException;

    BookingDTO updateBooking(BookingDTO booking) throws BookingNotFoundException, BookingConflictException;
}

