package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class BookingRepositoryCollection implements BookingRepository {

    private static final BookingRepositoryCollection INSTANCE = new BookingRepositoryCollection();

    private BookingRepositoryCollection() {
    }

    public static BookingRepositoryCollection getInstance() {
        return INSTANCE;
    }

    private final Map<Long, Booking> bookingsById = new HashMap<>();
    private final Map<Long, List<Booking>> bookingsByUserId = new HashMap<>();
    private final Map<Long, List<Booking>> bookingsByCoworkingId = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public Optional<Booking> createBooking(Booking newBooking) throws BookingConflictException, RepositoryException {
        if (isBookingOverlapping(newBooking)) {
            throw new BookingConflictException("Время бронирования совпадает с существующим бронированием");
        }

        Booking bookingFromDB = new Booking(idCounter.incrementAndGet(),newBooking.userId(), newBooking.coworkingId(), newBooking.date(), newBooking.timeSlots());

        addBookingToCollections(bookingFromDB);

        return Optional.ofNullable(bookingsById.get(bookingFromDB.id()));
    }



    @Override
    public Optional<Booking> updateBooking(Booking updatedBooking) throws BookingNotFoundException, BookingConflictException, RepositoryException {
        if (!bookingsById.containsKey(updatedBooking.id())) {
            throw new BookingNotFoundException("Бронирование не найдено");
        }

        Booking existingBooking = bookingsById.get(updatedBooking.id());

        if (!isBookingReduced(existingBooking, updatedBooking)){
            if (isBookingOverlapping(updatedBooking))
                throw new BookingConflictException("Время бронирования совпадает с существующим бронированием");
        }

        removeBookingFromCollections(existingBooking);
        addBookingToCollections(updatedBooking);

        return Optional.ofNullable(bookingsById.get(updatedBooking.id()));
    }


    @Override
    public void deleteBooking(long bookingId) throws BookingNotFoundException {
        Booking booking = bookingsById.remove(bookingId);
        if (booking != null) {
            bookingsByUserId.get(booking.userId()).remove(booking);
            bookingsByCoworkingId.get(booking.coworkingId()).remove(booking);
        } else {
            throw new BookingNotFoundException("Бронирование для удаления не найдено");
        }
    }

    @Override
    public Collection<Booking> getBookingsByUser(long userId) throws BookingNotFoundException {
        Collection<Booking> userBookings = bookingsByUserId.get(userId);
        if (userBookings == null || userBookings.isEmpty()) {
            throw new BookingNotFoundException("Бронирования пользователя не найдено");
        }
        return userBookings;
    }

    @Override
    public Collection<Booking> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException {
        Collection<Booking> userBookings = bookingsByUserId.get(userId);
        if (userBookings == null || userBookings.isEmpty()) {
            throw new BookingNotFoundException("Бронирования пользователя не найдено");
        }

        List<Booking> bookingsOnDate = userBookings.stream()
                .filter(booking -> booking.date().equals(date))
                .toList();

        if (bookingsOnDate.isEmpty()) {
            throw new BookingNotFoundException("У пользователя нет бронирований на эту дату: " + date);
        }

        return bookingsOnDate;
    }

    @Override
    public Collection<Booking> getBookingsByUserAndCoworking(long userId, long coworkingId) throws BookingNotFoundException {
        Collection<Booking> userBookings = bookingsByUserId.get(userId);
        if (userBookings == null || userBookings.isEmpty()) {
            throw new BookingNotFoundException("Бронирования пользователя не найдено");
        }

        Collection<Booking> bookingsInCoworking = userBookings.stream()
                .filter(booking -> booking.coworkingId() == coworkingId)
                .toList();

        if (bookingsInCoworking.isEmpty()) {
            throw new BookingNotFoundException("У пользователя нет бронирований в этом коворкинге");
        }

        return bookingsInCoworking;
    }

    @Override
    public Collection<Booking> getBookingsByCoworkingAndDate(long coworkingId, LocalDate date) {
        Collection<Booking> coworkingBookings = bookingsByCoworkingId.get(coworkingId);

        if (coworkingBookings == null || coworkingBookings.isEmpty()) return new ArrayList<>();


        return coworkingBookings.stream()
                .filter(booking -> booking.date().equals(date))
                .toList();
    }

    @Override
    public Optional<Booking> getBookingById(long bookingId) throws BookingNotFoundException {
        return Optional.ofNullable(bookingsById.get(bookingId));
    }

    @Override
    public void deleteAllCoworkingBookings(long coworkingId) {
        List<Booking> bookingsToRemove = bookingsByCoworkingId.getOrDefault(coworkingId, Collections.emptyList());

        for (Booking booking : bookingsToRemove) {
            bookingsById.remove(booking.id());
        }

        for (long userId : bookingsByUserId.keySet()) {
            bookingsByUserId.get(userId).removeIf(booking -> booking.coworkingId() == coworkingId);
        }
        bookingsByCoworkingId.remove(coworkingId);
    }


    private boolean isBookingOverlapping(Booking newBooking) {
        List<Booking> existingBookingsByCoworking = bookingsByCoworkingId.getOrDefault(newBooking.coworkingId(), Collections.emptyList());
        List<Booking> existingBookingsByCoworkingAndDate = existingBookingsByCoworking.stream()
                .filter(existingBooking -> existingBooking.date().equals(newBooking.date()))
                .toList();
        for (Booking existingBooking : existingBookingsByCoworkingAndDate) {
            boolean hasOverlap = newBooking.timeSlots().stream()
                    .anyMatch(newTimeSlot -> existingBooking.timeSlots().contains(newTimeSlot));
            if (hasOverlap) {
                return true;
            }
        }
        return false;
    }

    private boolean isBookingReduced(Booking existingBooking, Booking updatedBooking) {
        return new HashSet<>(existingBooking.timeSlots()).containsAll(updatedBooking.timeSlots());
    }

    private void removeBookingFromCollections(Booking booking) {
        bookingsByUserId.get(booking.userId()).remove(booking);
        bookingsByCoworkingId.get(booking.coworkingId()).remove(booking);
    }

    private void addBookingToCollections(Booking booking) {
        bookingsById.put(booking.id(), booking);
        bookingsByUserId.computeIfAbsent(booking.userId(), k -> new ArrayList<>()).add(booking);
        bookingsByCoworkingId.computeIfAbsent(booking.coworkingId(), k -> new ArrayList<>()).add(booking);
    }
}
