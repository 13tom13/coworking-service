package rest.repositories;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.out.entity.model.Booking;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.implementation.BookingRepositoryJdbc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.TestcontainersConnector;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DisplayName("Тесты репозитория бронирований")
class BookingRepositoryJdbcTest extends TestcontainersConnector {

    @Autowired
    private BookingRepositoryJdbc bookingRepository;

    private Booking booking;

    @BeforeEach
    void setUp() {
        long coworkingId = 1L;
        long userId = 1L;
        booking = new Booking(0L, userId, coworkingId, LocalDate.now(), List.of(TimeSlot.MORNING));
    }

    @Test
    @DisplayName("Тест создание бронирования")
    void createBooking_ShouldCreateBooking_WhenValidDataProvided() throws RepositoryException, BookingConflictException {
        Optional<Booking> createdBooking = bookingRepository.createBooking(booking);


        Assertions.assertThat(createdBooking).isPresent();
        Assertions.assertThat(createdBooking.get().id()).isGreaterThan(0);
        Assertions.assertThat(createdBooking.get().userId()).isEqualTo(booking.userId());
        Assertions.assertThat(createdBooking.get().coworkingId()).isEqualTo(booking.coworkingId());
    }

    @Test
    @DisplayName("Тест обновления бронирования")
    void updateBooking_ShouldUpdateBooking_WhenBookingExists() throws RepositoryException, BookingConflictException, BookingNotFoundException {
        Booking createdBooking = bookingRepository.createBooking(booking).get();
        Booking updatedBooking = new Booking(createdBooking.id(), createdBooking.userId(), createdBooking.coworkingId(), createdBooking.date(), List.of(TimeSlot.AFTERNOON));

        Optional<Booking> result = bookingRepository.updateBooking(updatedBooking);

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().timeSlots()).contains(TimeSlot.AFTERNOON);
    }

    @Test
    @DisplayName("Тест удаления бронирования")
    void deleteBooking_ShouldDeleteBooking_WhenBookingExists() throws RepositoryException, BookingNotFoundException, BookingConflictException {
        Booking createdBooking = bookingRepository.createBooking(booking).get();

        bookingRepository.deleteBooking(createdBooking.id());

        Assertions.assertThatThrownBy(() -> bookingRepository.getBookingById(createdBooking.id()))
                .isInstanceOf(BookingNotFoundException.class);
    }

    @Test
    @DisplayName("Тест получения бронирования по ID")
    void getBookingById_ShouldReturnBooking_WhenBookingExists() throws RepositoryException, BookingNotFoundException, BookingConflictException {
        Booking createdBooking = bookingRepository.createBooking(booking).get();

        Optional<Booking> foundBooking = bookingRepository.getBookingById(createdBooking.id());

        Assertions.assertThat(foundBooking).isPresent();
        Assertions.assertThat(foundBooking.get().id()).isEqualTo(createdBooking.id());
    }

    @Test
    @DisplayName("Тест получения бронирований пользователя")
    void getBookingsByUser_ShouldReturnBookings_WhenUserHasBookings() throws RepositoryException, BookingNotFoundException, BookingConflictException {
        bookingRepository.createBooking(booking);

        var bookings = bookingRepository.getBookingsByUser(booking.userId());

        Assertions.assertThat(bookings).isNotEmpty();
        Assertions.assertThat(bookings.iterator().next().userId()).isEqualTo(booking.userId());
    }

    @Test
    @DisplayName("Тест получения бронирований пользователя по дате")
    void getBookingsByUserAndDate_ShouldReturnBookings_WhenUserHasBookingsOnDate() throws RepositoryException, BookingNotFoundException, BookingConflictException {
        bookingRepository.createBooking(booking);

        var bookings = bookingRepository.getBookingsByUserAndDate(booking.userId(), booking.date());

        Assertions.assertThat(bookings).isNotEmpty();
        Assertions.assertThat(bookings.iterator().next().date()).isEqualTo(booking.date());
    }

    @Test
    @DisplayName("Тест получения бронирований пользователя по коворкингу")
    void getBookingsByUserAndCoworking_ShouldReturnBookings_WhenUserHasBookingsAtCoworking() throws RepositoryException, BookingNotFoundException, BookingConflictException {
        bookingRepository.createBooking(booking);

        var bookings = bookingRepository.getBookingsByUserAndCoworking(booking.userId(), booking.coworkingId());

        Assertions.assertThat(bookings).isNotEmpty();
        Assertions.assertThat(bookings.iterator().next().coworkingId()).isEqualTo(booking.coworkingId());
    }

    @Test
    @DisplayName("Тест получения бронирований по коворкингу и дате")
    void getBookingsByCoworkingAndDate_ShouldReturnBookings_WhenBookingsExist() throws RepositoryException, BookingConflictException {
        bookingRepository.createBooking(booking);

        var bookings = bookingRepository.getBookingsByCoworkingAndDate(booking.coworkingId(), booking.date());

        Assertions.assertThat(bookings).isNotEmpty();
        Assertions.assertThat(bookings.iterator().next().date()).isEqualTo(booking.date());
    }

}