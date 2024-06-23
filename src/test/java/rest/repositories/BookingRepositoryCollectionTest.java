package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingRepositoryCollectionTest {

    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository = BookingRepositoryCollection.getInstance();
    }

    @Test
    void testCreateBooking() throws BookingConflictException {
        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(1L)
                .coworkingId(1L)
                .date(LocalDate.now().plusDays(1))
                .timeSlot(TimeSlot.MORNING)
                .build();

        BookingDTO createdBooking = bookingRepository.createBooking(bookingDTO);

        assertThat(createdBooking).isNotNull();
        assertThat(createdBooking.id()).isNotNull();
    }

    @Test
    void testCreateBookingWithPastDate() {
        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(1L)
                .coworkingId(1L)
                .date(LocalDate.now().minusDays(1))
                .timeSlot(TimeSlot.MORNING)
                .build();

        assertThrows(BookingConflictException.class, () -> bookingRepository.createBooking(bookingDTO));
    }

    @Test
    void testUpdateBooking() throws BookingNotFoundException, BookingConflictException {
        BookingDTO bookingDTO = BookingDTO.builder()
                .id(1L)
                .userId(1L)
                .coworkingId(1L)
                .date(LocalDate.now().plusDays(2))
                .timeSlot(TimeSlot.MORNING)
                .build();

        BookingDTO createdBooking = bookingRepository.createBooking(bookingDTO);

        bookingDTO = BookingDTO.builder()
                .id(createdBooking.id())
                .userId(1L)
                .coworkingId(1L)
                .date(LocalDate.now().plusDays(2))
                .timeSlot(TimeSlot.AFTERNOON)
                .build();

        BookingDTO updatedBooking = bookingRepository.updateBooking(bookingDTO);

        assertThat(updatedBooking).isNotNull();
        assertThat(updatedBooking.id()).isEqualTo(createdBooking.id());
        assertThat(updatedBooking.timeSlots()).containsExactly(TimeSlot.AFTERNOON);
    }

    @Test
    void testUpdateNonExistingBooking() {
        BookingDTO bookingDTO = BookingDTO.builder()
                .id(999L)
                .userId(1L)
                .coworkingId(1L)
                .date(LocalDate.now().plusDays(2))
                .timeSlot(TimeSlot.MORNING)
                .build();

        assertThrows(BookingNotFoundException.class, () -> bookingRepository.updateBooking(bookingDTO));
    }

    @Test
    void testDeleteBooking() throws BookingNotFoundException, BookingConflictException {
        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(1L)
                .coworkingId(1L)
                .date(LocalDate.now().plusDays(3))
                .timeSlot(TimeSlot.MORNING)
                .build();

        BookingDTO createdBooking = bookingRepository.createBooking(bookingDTO);

        bookingRepository.deleteBooking(createdBooking.id());

        assertThrows(BookingNotFoundException.class, () -> bookingRepository.getBookingById(createdBooking.id()));
    }

    @Test
    void testGetBookingsByUser() throws BookingNotFoundException, BookingConflictException {
        long userId = 1L;

        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(userId)
                .coworkingId(1L)
                .date(LocalDate.now().plusDays(4))
                .timeSlot(TimeSlot.MORNING)
                .build();

        bookingRepository.createBooking(bookingDTO);

        List<BookingDTO> bookingsByUser = bookingRepository.getBookingsByUser(userId);

        assertThat(bookingsByUser).isNotEmpty();
    }

    @Test
    void testGetBookingsByUserAndDate() throws BookingNotFoundException, BookingConflictException {
        long userId = 1L;
        LocalDate date = LocalDate.now().plusDays(5);

        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(userId)
                .coworkingId(1L)
                .date(date)
                .timeSlot(TimeSlot.MORNING)
                .build();

        bookingRepository.createBooking(bookingDTO);

        List<BookingDTO> bookingsByUserAndDate = bookingRepository.getBookingsByUserAndDate(userId, date);

        assertThat(bookingsByUserAndDate).isNotEmpty();
    }

    @Test
    void testGetBookingsByUserAndCoworking() throws BookingNotFoundException, BookingConflictException {
        long userId = 1L;
        long coworkingId = 1L;

        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(userId)
                .coworkingId(coworkingId)
                .date(LocalDate.now().plusDays(6))
                .timeSlot(TimeSlot.MORNING)
                .build();

        bookingRepository.createBooking(bookingDTO);

        List<BookingDTO> bookingsByUserAndCoworking = bookingRepository.getBookingsByUserAndCoworking(userId, coworkingId);

        assertThat(bookingsByUserAndCoworking).isNotEmpty();
    }

    @Test
    void testGetBookingsByCoworkingAndDate() {
        long coworkingId = 1L;
        LocalDate date = LocalDate.now().plusDays(7);

        List<BookingDTO> bookingsByCoworkingAndDate = bookingRepository.getBookingsByCoworkingAndDate(coworkingId, date);

        assertThat(bookingsByCoworkingAndDate).isEmpty();
    }

    @Test
    void testGetBookingById() throws BookingNotFoundException, BookingConflictException {
        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(1L)
                .coworkingId(1L)
                .date(LocalDate.now().plusDays(8))
                .timeSlot(TimeSlot.MORNING)
                .build();

        BookingDTO createdBooking = bookingRepository.createBooking(bookingDTO);

        BookingDTO retrievedBooking = bookingRepository.getBookingById(createdBooking.id());

        assertThat(retrievedBooking).isNotNull();
        assertThat(retrievedBooking.id()).isEqualTo(createdBooking.id());
    }

    @Test
    void testDeleteAllCoworkingBookings() throws BookingConflictException {
        long coworkingId = 1L;

        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(1L)
                .coworkingId(coworkingId)
                .date(LocalDate.now().plusDays(9))
                .timeSlot(TimeSlot.MORNING)
                .build();

        BookingDTO createdBooking = bookingRepository.createBooking(bookingDTO);

        bookingRepository.deleteAllCoworkingBookings(coworkingId);

        assertThrows(BookingNotFoundException.class, () -> bookingRepository.getBookingById(createdBooking.id()));
    }
}
