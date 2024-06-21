package io.ylab.tom13.coworkingservice.in.rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingService {

    Booking createBooking(BookingDTO bookingDTO) throws BookingConflictException;

    void cancelBooking(long bookingId) throws BookingNotFoundException;

    Collection<Booking> getAllBookings();

    Collection<Booking> getBookingsByUser(long userId);

    Collection<Booking> getBookingsBySpace(long spaceId);

    Collection<Booking> getBookingsByDate(LocalDateTime date);
}

