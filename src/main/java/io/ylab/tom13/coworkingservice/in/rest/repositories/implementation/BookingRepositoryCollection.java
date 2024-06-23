package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.utils.BookingMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class BookingRepositoryCollection implements BookingRepository {

    private static final BookingRepositoryCollection INSTANCE = new BookingRepositoryCollection();

    private BookingRepositoryCollection() {
    }

    public static BookingRepositoryCollection getInstance() {
        return INSTANCE;
    }

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    private final Map<Long, Booking> bookingsById = new HashMap<>();
    private final Map<Long, List<Booking>> bookingsByUserId = new HashMap<>();
    private final Map<Long, List<Booking>> bookingsByCoworkingId = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) throws BookingConflictException {
        long bookingId = idCounter.incrementAndGet();
        Booking newBooking = bookingMapper.toBooking(bookingDTO, bookingId);

        if (bookingDTO.date().isBefore(LocalDate.now())) {
            throw new BookingConflictException("Время бронирования не может быть в прошлом");
        }

        if (isBookingOverlapping(newBooking)) {
            throw new BookingConflictException("Время бронирования совпадает с существующим бронированием");
        }

        bookingsById.put(newBooking.id(), newBooking);
        bookingsByUserId.computeIfAbsent(newBooking.userId(), k -> new ArrayList<>()).add(newBooking);
        bookingsByCoworkingId.computeIfAbsent(newBooking.coworkingId(), k -> new ArrayList<>()).add(newBooking);

        return bookingMapper.toBookingDTO(newBooking);
    }

    @Override
    public BookingDTO updateBooking(BookingDTO bookingDTO) throws BookingNotFoundException, BookingConflictException {
        Booking existingBooking = bookingsById.get(bookingDTO.id());
        if (existingBooking == null) {
            throw new BookingNotFoundException("Бронирование не найдено");
        }

        removeBookingFromCollections(existingBooking);

        Booking updatedBooking = bookingMapper.toBooking(bookingDTO, bookingDTO.id());

        if (isBookingOverlapping(updatedBooking)) {
            addBookingToCollections(existingBooking);
            throw new BookingConflictException("Время бронирования совпадает с существующим бронированием");
        }

        bookingsById.put(updatedBooking.id(), updatedBooking);
        addBookingToCollections(updatedBooking);

        return bookingMapper.toBookingDTO(updatedBooking);
    }

    @Override
    public void deleteBooking(long bookingId) throws BookingNotFoundException {
        Booking booking = bookingsById.remove(bookingId);
        if (booking != null) {
            bookingsByUserId.get(booking.userId()).remove(booking);
            bookingsByCoworkingId.get(booking.coworkingId()).remove(booking);
        } else {
            throw new BookingNotFoundException("Бронирование не найдено");
        }
    }

    @Override
    public List<BookingDTO> getBookingsByUser(long userId) throws BookingNotFoundException {
        List<Booking> userBookings = bookingsByUserId.get(userId);
        if (userBookings == null || userBookings.isEmpty()) {
            throw new BookingNotFoundException("Бронирования пользователя не найдено");
        }
        return userBookings.stream()
                .map(bookingMapper::toBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException {
        List<Booking> userBookings = bookingsByUserId.get(userId);
        if (userBookings == null || userBookings.isEmpty()) {
            throw new BookingNotFoundException("Бронирования пользователя не найдено");
        }

        List<Booking> bookingsOnDate = userBookings.stream()
                .filter(booking -> booking.date().equals(date))
                .toList();

        if (bookingsOnDate.isEmpty()) {
            throw new BookingNotFoundException("У пользователя нет бронирований на эту дату: " + date);
        }

        return bookingsOnDate.stream()
                .map(bookingMapper::toBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByUserAndCoworking(long userId, long coworkingId) throws BookingNotFoundException {
        List<Booking> userBookings = bookingsByUserId.get(userId);
        if (userBookings == null || userBookings.isEmpty()) {
            throw new BookingNotFoundException("Бронирования пользователя не найдено");
        }

        List<Booking> bookingsInCoworking = userBookings.stream()
                .filter(booking -> booking.coworkingId()==coworkingId)
                .toList();

        if (bookingsInCoworking.isEmpty()) {
            throw new BookingNotFoundException("У пользователя нет бронирований в этом коворкинге");
        }

        return bookingsInCoworking.stream()
                .map(bookingMapper::toBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByCoworkingAndDate(long coworkingId, LocalDate date) {
        List<Booking> coworkingBookings = bookingsByCoworkingId.get(coworkingId);
        if (coworkingBookings == null || coworkingBookings.isEmpty()) {
            return new ArrayList<>();
        }
        return coworkingBookings.stream()
                .filter(booking -> booking.date().equals(date))
                .map(bookingMapper::toBookingDTO)
                .toList();
    }

    @Override
    public BookingDTO getBookingById(long bookingId) throws BookingNotFoundException {
        Booking booking = bookingsById.get(bookingId);
        if (booking == null) throw new BookingNotFoundException("Бронирования пользователя не найдено");
        return bookingMapper.toBookingDTO(booking);
    }

    private boolean isBookingOverlapping(Booking newBooking) {
        List<Booking> existingBookings = bookingsByCoworkingId.getOrDefault(newBooking.coworkingId(), Collections.emptyList());
        for (Booking existingBooking : existingBookings) {
            if (!existingBooking.date().equals(newBooking.date())) {
                continue;
            }
            for (TimeSlot newSlot : newBooking.timeSlots()) {
                for (TimeSlot existingSlot : existingBooking.timeSlots()) {
                    if (doTimeSlotsOverlap(newSlot, existingSlot)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean doTimeSlotsOverlap(TimeSlot slot1, TimeSlot slot2) {
        return slot1.getEnd().isAfter(slot2.getStart()) && slot2.getEnd().isAfter(slot1.getStart());
    }

    private void removeBookingFromCollections(Booking booking) {
        bookingsByUserId.get(booking.userId()).remove(booking);
        bookingsByCoworkingId.get(booking.coworkingId()).remove(booking);
    }

    private void addBookingToCollections(Booking booking) {
        bookingsByUserId.computeIfAbsent(booking.userId(), k -> new ArrayList<>()).add(booking);
        bookingsByCoworkingId.computeIfAbsent(booking.coworkingId(), k -> new ArrayList<>()).add(booking);
    }
}
