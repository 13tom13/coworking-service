package io.ylab.tom13.coworkingservice.in.rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDTO createBooking(BookingDTO bookingDTO) throws BookingConflictException, RepositoryException;

    void cancelBooking(long bookingId) throws BookingNotFoundException;

    List<BookingDTO> getBookingsByUser(long userId) throws BookingNotFoundException;

    List<BookingDTO> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException;

    List<BookingDTO> getBookingsByUserAndCoworking(long userId, long coworkingId) throws BookingNotFoundException;

    List<TimeSlot> getAvailableSlots(long coworkingId, LocalDate date);

    BookingDTO getBookingById(long bookingId) throws BookingNotFoundException;

    BookingDTO updateBooking(BookingDTO booking) throws BookingNotFoundException, BookingConflictException, RepositoryException;

}

