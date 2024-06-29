package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория бронирований с использованием JDBC.
 */
public class BookingRepositoryJdbc implements BookingRepository {

    private final Connection connection;

    public BookingRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Booking> createBooking(Booking newBooking) throws BookingConflictException, RepositoryException {
        String insertBookingSql = """
                INSERT INTO main.bookings (user_id, coworking_id, date)
                VALUES (?, ?, ?)
                """;

        String insertBookingTimeSlotsSql = """
                INSERT INTO relations.booking_time_slots (booking_id, time_slot_id)
                VALUES (?, ?)
                """;

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement insertBookingStatement = connection.prepareStatement(insertBookingSql, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement insertBookingTimeSlotsStatement = connection.prepareStatement(insertBookingTimeSlotsSql)) {

                insertBookingStatement.setLong(1, newBooking.userId());
                insertBookingStatement.setLong(2, newBooking.coworkingId());
                insertBookingStatement.setObject(3, newBooking.date());

                int affectedRows = insertBookingStatement.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    throw new RepositoryException("Создание бронирования не удалось, нет затронутых строк.");
                }

                try (ResultSet generatedKeys = insertBookingStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long bookingId = generatedKeys.getLong(1);

//                        for (TimeSlot timeSlotId : newBooking.timeSlots()) {
//                            insertBookingTimeSlotsStatement.setLong(1, bookingId);
//                            insertBookingTimeSlotsStatement.setLong(2, timeSlotId);
//                            insertBookingTimeSlotsStatement.addBatch();
//                        }

                        insertBookingTimeSlotsStatement.executeBatch();
                        connection.commit();

                        return Optional.of(new Booking(bookingId, newBooking.userId(), newBooking.coworkingId(), newBooking.date(), newBooking.timeSlots()));
                    } else {
                        connection.rollback();
                        throw new RepositoryException("Создание бронирования не удалось, ID не был получен.");
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                if (e.getSQLState().equals("23505")) { // unique_violation
                    throw new BookingConflictException("Время бронирования совпадает с существующим бронированием");
                } else {
                    throw new RepositoryException("Ошибка при создании бронирования: " + e.getMessage());
                }
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при соединении с базой данных: " + e.getMessage());
        }
    }

    @Override
    public Optional<Booking> updateBooking(Booking updatedBooking) throws BookingNotFoundException, BookingConflictException, RepositoryException {
        String updateBookingSql = """
                UPDATE main.bookings
                SET user_id = ?, coworking_id = ?, date = ?
                WHERE id = ?
                """;

        String deleteBookingTimeSlotsSql = """
                DELETE FROM relations.booking_time_slots
                WHERE booking_id = ?
                """;

        String insertBookingTimeSlotsSql = """
                INSERT INTO relations.booking_time_slots (booking_id, time_slot_id)
                VALUES (?, ?)
                """;

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement updateBookingStatement = connection.prepareStatement(updateBookingSql);
                 PreparedStatement deleteBookingTimeSlotsStatement = connection.prepareStatement(deleteBookingTimeSlotsSql);
                 PreparedStatement insertBookingTimeSlotsStatement = connection.prepareStatement(insertBookingTimeSlotsSql)) {

                updateBookingStatement.setLong(1, updatedBooking.userId());
                updateBookingStatement.setLong(2, updatedBooking.coworkingId());
                updateBookingStatement.setObject(3, updatedBooking.date());
                updateBookingStatement.setLong(4, updatedBooking.id());

                int affectedRows = updateBookingStatement.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    throw new BookingNotFoundException("Бронирование не найдено");
                }

                deleteBookingTimeSlotsStatement.setLong(1, updatedBooking.id());
                deleteBookingTimeSlotsStatement.executeUpdate();

//                for (String timeSlotId : updatedBooking.timeSlots()) {
//                    insertBookingTimeSlotsStatement.setLong(1, updatedBooking.id());
//                    insertBookingTimeSlotsStatement.setLong(2, Long.parseLong(timeSlotId));
//                    insertBookingTimeSlotsStatement.addBatch();
//                }

                insertBookingTimeSlotsStatement.executeBatch();
                connection.commit();

                return Optional.of(updatedBooking);

            } catch (SQLException e) {
                connection.rollback();
                if (e.getSQLState().equals("23505")) { // unique_violation
                    throw new BookingConflictException("Время бронирования совпадает с существующим бронированием");
                } else {
                    throw new RepositoryException("Ошибка при обновлении бронирования: " + e.getMessage());
                }
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при соединении с базой данных: " + e.getMessage());
        }
    }

    @Override
    public void deleteBooking(long bookingId) throws BookingNotFoundException, RepositoryException {
        String deleteBookingSql = "DELETE FROM main.bookings WHERE id = ?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteBookingStatement = connection.prepareStatement(deleteBookingSql)) {
                deleteBookingStatement.setLong(1, bookingId);

                int affectedRows = deleteBookingStatement.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    throw new BookingNotFoundException("Бронирование для удаления не найдено");
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RepositoryException("Ошибка при удалении бронирования: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при соединении с базой данных: " + e.getMessage());
        }
    }

    @Override
    public Collection<Booking> getBookingsByUser(long userId) throws BookingNotFoundException, RepositoryException {
        String selectBookingsSql = """
                SELECT b.id, b.user_id, b.coworking_id, b.date, array_agg(bts.time_slot_id::text) AS time_slots
                FROM main.bookings b
                         LEFT JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.user_id = ?
                GROUP BY b.id, b.user_id, b.coworking_id, b.date
                """;

        try {
            List<Booking> bookings = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(selectBookingsSql)) {
                statement.setLong(1, userId);
                ResultSet resultSet = statement.executeQuery();

//                while (resultSet.next()) {
//                    long id = resultSet.getLong("id");
//                    long coworkingId = resultSet.getLong("coworking_id");
//                    LocalDate date = resultSet.getObject("date", LocalDate.class);
//                    String[] timeSlotsArray = (String[]) resultSet.getArray("time_slots").getArray();
//                    List<String> timeSlots = timeSlotsArray == null ? new ArrayList<>() : List.of(timeSlotsArray);
//
////                    bookings.add(new Booking(id, userId, coworkingId, date, timeSlots));
//                }
            }
            if (bookings.isEmpty()) {
                throw new BookingNotFoundException("Бронирования пользователя не найдены");
            }
            return bookings;
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    @Override
    public Collection<Booking> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException, RepositoryException {
        String selectBookingsSql = """
                SELECT b.id, b.user_id, b.coworking_id, b.date, array_agg(bts.time_slot_id::text) AS time_slots
                FROM main.bookings b
                         LEFT JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.user_id = ? AND b.date = ?
                GROUP BY b.id, b.user_id, b.coworking_id, b.date
                """;

        try {
            List<Booking> bookings = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(selectBookingsSql)) {
                statement.setLong(1, userId);
                statement.setObject(2, date);
                ResultSet resultSet = statement.executeQuery();

//                while (resultSet.next()) {
//                    long id = resultSet.getLong("id");
//                    long coworkingId = resultSet.getLong("coworking_id");
//                    LocalDate bookingDate = resultSet.getObject("date", LocalDate.class);
//                    String[] timeSlotsArray = (String[]) resultSet.getArray("time_slots").getArray();
//                    List<String> timeSlots = timeSlotsArray == null ? new ArrayList<>() : List.of(timeSlotsArray);
//
//                    bookings.add(new Booking(id, userId, coworkingId, bookingDate, timeSlots));
//                }
            }
            if (bookings.isEmpty()) {
                throw new BookingNotFoundException("У пользователя нет бронирований на эту дату: " + date);
            }
            return bookings;
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    @Override
    public Collection<Booking> getBookingsByUserAndCoworking(long userId, long coworkingId) throws BookingNotFoundException, RepositoryException {
        String selectBookingsSql = """
                SELECT b.id, b.user_id, b.coworking_id, b.date, array_agg(bts.time_slot_id::text) AS time_slots
                FROM main.bookings b
                         LEFT JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.user_id = ? AND b.coworking_id = ?
                GROUP BY b.id, b.user_id, b.coworking_id, b.date
                """;

        try {
            List<Booking> bookings = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(selectBookingsSql)) {
                statement.setLong(1, userId);
                statement.setLong(2, coworkingId);
                ResultSet resultSet = statement.executeQuery();

//                while (resultSet.next()) {
//                    long id = resultSet.getLong("id");
//                    LocalDate bookingDate = resultSet.getObject("date", LocalDate.class);
//                    String[] timeSlotsArray = (String[]) resultSet.getArray("time_slots").getArray();
//                    List<String> timeSlots = timeSlotsArray == null ? new ArrayList<>() : List.of(timeSlotsArray);
//
//                    bookings.add(new Booking(id, userId, coworkingId, bookingDate, timeSlots));
//                }
            }
            if (bookings.isEmpty()) {
                throw new BookingNotFoundException("У пользователя нет бронирований в этом коворкинге");
            }
            return bookings;
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    @Override
    public Collection<Booking> getBookingsByCoworkingAndDate(long coworkingId, LocalDate date) throws RepositoryException {
        String selectBookingsSql = """
                SELECT b.id, b.user_id, b.coworking_id, b.date, array_agg(bts.time_slot_id::text) AS time_slots
                FROM main.bookings b
                         LEFT JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.coworking_id = ? AND b.date = ?
                GROUP BY b.id, b.user_id, b.coworking_id, b.date
                """;

        try {
            List<Booking> bookings = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(selectBookingsSql)) {
                statement.setLong(1, coworkingId);
                statement.setObject(2, date);
                ResultSet resultSet = statement.executeQuery();

//                while (resultSet.next()) {
//                    long id = resultSet.getLong("id");
//                    long userId = resultSet.getLong("user_id");
//                    String[] timeSlotsArray = (String[]) resultSet.getArray("time_slots").getArray();
//                    List<String> timeSlots = timeSlotsArray == null ? new ArrayList<>() : List.of(timeSlotsArray);
//
//                    bookings.add(new Booking(id, userId, coworkingId, date, timeSlots));
//                }
            }
            return bookings;
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    @Override
    public Optional<Booking> getBookingById(long bookingId) throws BookingNotFoundException, RepositoryException {
        String selectBookingSql = """
                SELECT b.user_id, b.coworking_id, b.date, array_agg(bts.time_slot_id::text) AS time_slots
                FROM main.bookings b
                         LEFT JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.id = ?
                GROUP BY b.user_id, b.coworking_id, b.date
                """;

        try {
            try (PreparedStatement statement = connection.prepareStatement(selectBookingSql)) {
                statement.setLong(1, bookingId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    long userId = resultSet.getLong("user_id");
                    long coworkingId = resultSet.getLong("coworking_id");
                    LocalDate bookingDate = resultSet.getObject("date", LocalDate.class);
                    String[] timeSlotsArray = (String[]) resultSet.getArray("time_slots").getArray();
                    List<String> timeSlots = timeSlotsArray == null ? new ArrayList<>() : List.of(timeSlotsArray);

//                    return Optional.of(new Booking(bookingId, userId, coworkingId, bookingDate));
                    return Optional.empty();
                } else {
                    throw new BookingNotFoundException("Бронирование с указанным ID не найдено");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    @Override
    public void deleteAllCoworkingBookings(long coworkingId) {
        String deleteBookingsSql = "DELETE FROM main.bookings WHERE coworking_id = ?";
//        try {
//            connection.setAutoCommit(false);
//            try (PreparedStatement deleteBookingsStatement = connection.prepareStatement(deleteBookingsSql)) {
//                deleteBookingsStatement.setLong(1, coworkingId);
//
//                int affectedRows = deleteBookingsStatement.executeUpdate();
//
//                if (affectedRows == 0) {
//                    connection.rollback();
//                    throw new RepositoryException("Удаление бронирований для коворкинга не выполнено, нет затронутых строк.");
//                }
//
//                connection.commit();
//            } catch (SQLException e) {
//                connection.rollback();
//                throw new RepositoryException("Ошибка при удалении бронирований для коворкинга: " + e.getMessage());
//            } finally {
//                connection.setAutoCommit(true);
//            }
//        } catch (SQLException e) {
//            throw new RepositoryException("Ошибка при соединении с базой данных: " + e.getMessage());
//        }
    }

    // Приватные методы для проверок и управления состоянием

    private boolean isBookingOverlapping(Booking newBooking) {
        // Реализация метода isBookingOverlapping
        return false;
    }

    private boolean isBookingReduced(Booking existingBooking, Booking updatedBooking) {
        // Реализация метода isBookingReduced
        return false;
    }

    private void removeBookingFromCollections(Booking booking) {
        // Реализация метода removeBookingFromCollections
    }

    private void addBookingToCollections(Booking booking) {
        // Реализация метода addBookingToCollections
    }
}
