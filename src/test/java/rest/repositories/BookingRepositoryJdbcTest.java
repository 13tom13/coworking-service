package rest.repositories;

import database.TestcontainersConnector;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.BookingRepositoryJdbc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DisplayName("Тесты репозитория бронирований")
class BookingRepositoryJdbcTest extends TestcontainersConnector {

    private Connection connection;
    private BookingRepositoryJdbc bookingRepository;
    private Booking booking;
    private long userId;
    private long coworkingId;

    @BeforeEach
    void setUp() throws RepositoryException, UserAlreadyExistsException, CoworkingConflictException {
        connection = getConnection();
        bookingRepository = new BookingRepositoryJdbc(connection);
        User userForTest = new User(0, "John", "Doe", "john.new@example.com", "password", Role.USER);
        Workplace workplaceForTest = new Workplace(0L, "testWorkplace", "test", true, "Workplace");
        testUserToDB(userForTest);
        testCoworkingToDB(workplaceForTest);
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

    private void testUserToDB(User user) throws RepositoryException, UserAlreadyExistsException {
        String sql = """
                INSERT INTO main.users (first_name, last_name, email, password, role)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.firstName());
            statement.setString(2, user.lastName());
            statement.setString(3, user.email());
            statement.setString(4, user.password());
            statement.setString(5, user.role().name());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RepositoryException("Создание пользователя не удалось, нет затронутых строк.");
            }

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userId = generatedKeys.getLong(1);
                } else {
                    throw new RepositoryException("Создание пользователя не удалось, ID не был получен.");
                }
            }

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // unique_violation
                throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
            } else {
                throw new RepositoryException("Ошибка при создании пользователя: " + e.getMessage());
            }
        }
    }

    private void testCoworkingToDB(Workplace workplace) throws RepositoryException, CoworkingConflictException {
        String sql = """
                INSERT INTO main.coworkings (name, description, available, type, workplace_type, conference_room_capacity)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, workplace.getName());
            statement.setString(2, workplace.getDescription());
            statement.setBoolean(3, workplace.isAvailable());
            statement.setString(4, workplace.getType());
            statement.setNull(5, Types.VARCHAR);
            statement.setNull(6, Types.INTEGER);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RepositoryException("Создание коворкинга не удалось, нет затронутых строк.");
            }

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    coworkingId = generatedKeys.getLong(1);
                } else {
                    throw new RepositoryException("Создание коворкинга не удалось, ID не был получен.");
                }
            }

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // unique_violation
                throw new CoworkingConflictException("Имя коворкинга уже занято");
            } else {
                throw new RepositoryException("Ошибка при создании коворкинга: " + e.getMessage());
            }
        }
    }
}
