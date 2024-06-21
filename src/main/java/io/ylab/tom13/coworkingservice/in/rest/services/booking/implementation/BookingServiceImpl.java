package io.ylab.tom13.coworkingservice.in.rest.services.booking.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.BookingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.booking.BookingService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;


    public BookingServiceImpl() {
        this.bookingRepository = BookingRepositoryCollection.getInstance();
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) throws BookingConflictException {
        return bookingRepository.createBooking(bookingDTO);
    }

    @Override
    public void cancelBooking(long bookingId) throws BookingNotFoundException {
        bookingRepository.deleteBooking(bookingId);
    }

    @Override
    public List<BookingDTO> getBookingsByUser(long userId) throws BookingNotFoundException {
        return bookingRepository.getBookingsByUser(userId);
    }

    @Override
    public List<BookingDTO> getBookingsByCoworking(long coworkingId) throws BookingNotFoundException {
        return bookingRepository.getBookingsByCoworking(coworkingId);
    }

    @Override
    public List<BookingDTO> getBookingsByCoworkingAndDate(long coworkingId, LocalDate date) {
        return bookingRepository.getBookingsByCoworkingAndDate(coworkingId, date);
    }

    @Override
    public List<TimeSlot> getAvailableSlots(long coworkingId, LocalDate date) {
        List<BookingDTO> bookings = new ArrayList<>(bookingRepository.getBookingsByCoworkingAndDate(coworkingId, date));
        List<TimeSlot> availableSlots = new ArrayList<>(Arrays.asList(TimeSlot.values()));

        if (bookings.isEmpty()) {
            return availableSlots;
        }

        for (BookingDTO booking : bookings) {
            for (TimeSlot bookedSlot : booking.timeSlots()) {
                availableSlots.remove(bookedSlot);
            }
        }

        return availableSlots;
    }

}

