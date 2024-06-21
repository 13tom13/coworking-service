package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class BookingRepositoryCollection implements BookingRepository {

    private static final BookingRepositoryCollection INSTANCE = new BookingRepositoryCollection();

    private BookingRepositoryCollection() {
    }

    /**
     * Получение единственного экземпляра класса репозитория.
     *
     * @return экземпляр BookingRepositoryCollection
     */
    public static BookingRepositoryCollection getInstance() {
        return INSTANCE;
    }

    private final Map<Long, Booking> bookingsById = new HashMap<>();
    private final Map<Long, List<Booking>> bookingsByUserId = new HashMap<>();
    private final Map<Long, List<Booking>> bookingsByCoworkingId = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) throws BookingConflictException {
        Booking newBooking = toBooking(bookingDTO, idCounter.incrementAndGet());

        if(bookingDTO.endTime().isBefore(LocalDateTime.now())){
            throw new BookingConflictException("Время бронирования не может быть в прошлом");
        }

        if (isBookingOverlapping(newBooking)) {
            throw new BookingConflictException("Время бронирования совпадает с существующим бронированием");
        }

        bookingsById.put(newBooking.getId(), newBooking);
        bookingsByUserId.computeIfAbsent(newBooking.getUserId(), k -> new ArrayList<>()).add(newBooking);
        bookingsByCoworkingId.computeIfAbsent(newBooking.getCoworkingId(), k -> new ArrayList<>()).add(newBooking);

        return toBookingDTO(newBooking);
    }

    @Override
    public BookingDTO updateBooking(BookingDTO bookingDTO) throws BookingNotFoundException, BookingConflictException {
        Booking existingBooking = bookingsById.get(bookingDTO.id());
        if (existingBooking == null) {
            throw new BookingNotFoundException("Бронирование не найдено");
        }

        removeBookingFromCollections(existingBooking);

        Booking updatedBooking = toBooking(bookingDTO, bookingDTO.id());

        if (isBookingOverlapping(updatedBooking)) {
            addBookingToCollections(existingBooking);
            throw new BookingConflictException("Время бронирования совпадает с существующим бронированием");
        }

        bookingsById.put(updatedBooking.getId(), updatedBooking);
        addBookingToCollections(updatedBooking);

        return toBookingDTO(updatedBooking);
    }

    @Override
    public void deleteBooking(long bookingId) throws BookingNotFoundException {
        Booking booking = bookingsById.remove(bookingId);
        if (booking != null) {
            bookingsByUserId.get(booking.getUserId()).remove(booking);
            bookingsByCoworkingId.get(booking.getCoworkingId()).remove(booking);
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
                .map(this::toBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByCoworking(long coworkingId) throws BookingNotFoundException {
        List<Booking> coworkingBookings = bookingsByCoworkingId.get(coworkingId);
        if (coworkingBookings == null || coworkingBookings.isEmpty()) {
            throw new BookingNotFoundException("Бронирований коворкинга не найдено");
        }
        return coworkingBookings.stream()
                .map(this::toBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByCoworkingAndDate(long coworkingId, LocalDate date) {
        List<Booking> coworkingBookings = bookingsByCoworkingId.get(coworkingId);
        if (coworkingBookings == null || coworkingBookings.isEmpty()) {
           return new ArrayList<>();
        }
        return coworkingBookings.stream()
                .filter(booking -> booking.getStartTime().toLocalDate().equals(date))
                .map(this::toBookingDTO)
                .toList();
    }

    private BookingDTO toBookingDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getUserId(),
                booking.getCoworkingId(),
                booking.getStartTime(),
                booking.getEndTime()
        );
    }

    private Booking toBooking(BookingDTO bookingDTO, long bookingId) {
        return new Booking(
                bookingId,
                bookingDTO.userId(),
                bookingDTO.coworkingId(),
                bookingDTO.startTime(),
                bookingDTO.endTime()
        );
    }

    private boolean isBookingOverlapping(Booking newBooking) {
        List<Booking> existingBookings = bookingsByCoworkingId.getOrDefault(newBooking.getCoworkingId(), Collections.emptyList());
        for (Booking existingBooking : existingBookings) {
            if (newBooking.getStartTime().isBefore(existingBooking.getEndTime()) &&
                newBooking.getEndTime().isAfter(existingBooking.getStartTime())) {
                return true;
            }
        }
        return false;
    }

    private void removeBookingFromCollections(Booking booking) {
        bookingsByUserId.get(booking.getUserId()).remove(booking);
        bookingsByCoworkingId.get(booking.getCoworkingId()).remove(booking);
    }

    private void addBookingToCollections(Booking booking) {
        bookingsByUserId.computeIfAbsent(booking.getUserId(), k -> new ArrayList<>()).add(booking);
        bookingsByCoworkingId.computeIfAbsent(booking.getCoworkingId(), k -> new ArrayList<>()).add(booking);
    }
}


