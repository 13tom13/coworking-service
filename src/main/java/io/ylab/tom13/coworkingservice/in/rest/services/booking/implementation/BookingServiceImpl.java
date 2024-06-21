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
import java.util.Comparator;
import java.util.List;

import static io.ylab.tom13.coworkingservice.in.config.BookingConfig.BOOKING_END_TIME;
import static io.ylab.tom13.coworkingservice.in.config.BookingConfig.BOOKING_START_TIME;

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
        LocalTime startOfDay = BOOKING_START_TIME;
        LocalTime endOfDay = BOOKING_END_TIME;
        List<BookingDTO> bookings = new ArrayList<>(bookingRepository.getBookingsByCoworkingAndDate(coworkingId, date));
        List<TimeSlot> availableSlots = new ArrayList<>();

        if (bookings.isEmpty()) {
            availableSlots.add(new TimeSlot(startOfDay, endOfDay));
            return availableSlots;
        }

        bookings.sort(Comparator.comparing(BookingDTO::startTime));

        LocalTime lastEndTime = startOfDay;

        for (BookingDTO booking : bookings) {
            LocalTime bookingStart = booking.startTime().toLocalTime();
            LocalTime bookingEnd = booking.endTime().toLocalTime();

            if (lastEndTime.isBefore(bookingStart)) {
                availableSlots.add(new TimeSlot(lastEndTime, bookingStart));
            }
            lastEndTime = bookingEnd;
        }

        if (lastEndTime.isBefore(endOfDay)) {
            availableSlots.add(new TimeSlot(lastEndTime, endOfDay));
        }

        return availableSlots;


    }
}

