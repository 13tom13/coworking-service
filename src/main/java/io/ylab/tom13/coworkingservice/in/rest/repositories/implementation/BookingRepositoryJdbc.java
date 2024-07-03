package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Реализация интерфейса {@link BookingRepository}.
 * Репозиторий бронирований с использованием JDBC.
 */
public class BookingRepositoryJdbc implements BookingRepository {

    private final Connection connection;

    public BookingRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Booking> createBooking(Booking newBooking) throws BookingConflictException, RepositoryException {
        try {
            connection.setAutoCommit(false);
            checkBookingOverlap(newBooking);
            String insertBookingSQL = """
                    INSERT INTO main.bookings (user_id, coworking_id, date) VALUES (?, ?, ?)
                    """;
            try (PreparedStatement bookingStatement = connection.prepareStatement(insertBookingSQL, Statement.RETURN_GENERATED_KEYS)) {
                bookingStatement.setLong(1, newBooking.userId());
                bookingStatement.setLong(2, newBooking.coworkingId());
                bookingStatement.setDate(3, Date.valueOf(newBooking.date()));

                int affectedRows = bookingStatement.executeUpdate();
                if (affectedRows == 0) {
                    connection.rollback();
                    throw new RepositoryException("Создание бронирования не удалось, нет затронутых строк.");
                }
                try (ResultSet generatedKeys = bookingStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long bookingId = generatedKeys.getLong(1);
                        insertTimeSlots(bookingId, newBooking);

                        connection.commit();
                        return Optional.of(new Booking(bookingId, newBooking.userId(), newBooking.coworkingId(), newBooking.date(), newBooking.timeSlots()));
                    } else {
                        connection.rollback();
                        throw new RepositoryException("Создание пользователя не удалось, ID не был получен.");
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new RepositoryException("Ошибка при создании бронирования: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при установке автокоммита: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Booking> updateBooking(Booking updatedBooking) throws BookingNotFoundException, BookingConflictException, RepositoryException {
        try {
            connection.setAutoCommit(false);
            long bookingId = updatedBooking.id();


            if (getBookingById(bookingId).isEmpty()) {
                throw new BookingNotFoundException("Бронирование с указанным ID не найдено");
            }

            String updateBookingSQL = """
                    UPDATE main.bookings
                    SET user_id = ?, coworking_id = ?, date = ?
                    WHERE id = ?
                    """;
            try (PreparedStatement bookingStatement = connection.prepareStatement(updateBookingSQL)) {
                bookingStatement.setLong(1, updatedBooking.userId());
                bookingStatement.setLong(2, updatedBooking.coworkingId());
                bookingStatement.setDate(3, Date.valueOf(updatedBooking.date()));
                bookingStatement.setLong(4, bookingId);

                int affectedRows = bookingStatement.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    throw new RepositoryException("Обновление бронирования не удалось, нет затронутых строк.");
                }


                deleteTimeSlots(bookingId);

                insertTimeSlots(bookingId, updatedBooking);

                checkBookingOverlap(updatedBooking);


                connection.commit();

                return Optional.of(updatedBooking);

            } catch (SQLException e) {
                connection.rollback();
                throw new RepositoryException("Ошибка при обновлении бронирования: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при установке автокоммита: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBooking(long bookingId) throws BookingNotFoundException, RepositoryException {
        try {
            connection.setAutoCommit(false);

            if (getBookingById(bookingId).isEmpty()) {
                throw new BookingNotFoundException("Бронирование с указанным ID не найдено");
            }

            deleteTimeSlots(bookingId);

            String deleteBookingSQL = """
                    DELETE FROM main.bookings
                    WHERE id = ?
                    """;
            try (PreparedStatement bookingStatement = connection.prepareStatement(deleteBookingSQL)) {
                bookingStatement.setLong(1, bookingId);

                int affectedRows = bookingStatement.executeUpdate();
                if (affectedRows == 0) {
                    connection.rollback();
                    throw new RepositoryException("Удаление бронирования не удалось, нет затронутых строк.");
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RepositoryException("Ошибка при удалении бронирования: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при установке автокоммита: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Booking> getBookingsByUser(long userId) throws BookingNotFoundException, RepositoryException {
        String query = """
                SELECT *
                FROM main.bookings b
                JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.user_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);

            Collection<Booking> userBookings = statementToBookings(statement);

            if (userBookings.isEmpty()) {
                throw new BookingNotFoundException("Бронирования пользователя не найдено");
            }

            return userBookings;

        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при получении бронирований пользователя: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Booking> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException, RepositoryException {
        String query = """
                SELECT *
                FROM main.bookings b
                JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.user_id = ? AND b.date = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.setDate(2, Date.valueOf(date));

            Collection<Booking> userBookings = statementToBookings(statement);

            if (userBookings.isEmpty()) {
                throw new BookingNotFoundException("Бронирования пользователя не найдено");
            }

            return userBookings;
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при получении бронирований пользователя и по дате: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Booking> getBookingsByUserAndCoworking(long userId, long coworkingId) throws BookingNotFoundException, RepositoryException {
        String query = """
                SELECT *
                FROM main.bookings b
                JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.user_id = ? AND b.coworking_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.setLong(2, coworkingId);

            Collection<Booking> bookings = statementToBookings(statement);

            if (bookings.isEmpty()) {
                throw new BookingNotFoundException("Бронирования пользователя в этом коворкинге не найдены");
            }

            return bookings;

        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при получении бронирований по пользователю и коворкингу: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Booking> getBookingsByCoworkingAndDate(long coworkingId, LocalDate date) throws RepositoryException {
        String query = """
                SELECT *
                FROM main.bookings b
                JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.coworking_id = ? AND b.date = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, coworkingId);
            statement.setDate(2, Date.valueOf(date));

            return statementToBookings(statement);

        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при получении бронирований по коворкингу и дате: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Booking> getBookingById(long bookingId) throws BookingNotFoundException, RepositoryException {
        String query = """
                SELECT *
                FROM main.bookings b
                JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, bookingId);

            Collection<Booking> bookings = statementToBookings(statement);

            if (bookings.isEmpty()) {
                throw new BookingNotFoundException("Бронирование с указанным ID не найдено");
            }

            return Optional.of(bookings.iterator().next());

        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при получении бронирования по ID: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllCoworkingBookings(long coworkingId) throws RepositoryException {
        try {
            connection.setAutoCommit(false);

            String selectBookingIdsSQL = """
                    SELECT id
                    FROM main.bookings
                    WHERE coworking_id = ?
                    """;
            try (PreparedStatement selectStatement = connection.prepareStatement(selectBookingIdsSQL)) {
                selectStatement.setLong(1, coworkingId);
                ResultSet rs = selectStatement.executeQuery();
                List<Long> bookingIds = new ArrayList<>();
                while (rs.next()) {
                    bookingIds.add(rs.getLong("id"));
                }

                for (Long bookingId : bookingIds) {
                    deleteTimeSlots(bookingId);
                }

                String deleteBookingsSQL = """
                        DELETE FROM main.bookings
                        WHERE coworking_id = ?
                        """;
                try (PreparedStatement bookingStatement = connection.prepareStatement(deleteBookingsSQL)) {
                    bookingStatement.setLong(1, coworkingId);
                    bookingStatement.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RepositoryException("Ошибка при удалении всех бронирований коворкинга: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при установке автокоммита: " + e.getMessage());
        }
    }


    /**
     * Проверяет пересечение бронирований по времени
     *
     * @param updatedBooking - ID бронирования
     */
    private void checkBookingOverlap(Booking updatedBooking) throws RepositoryException, BookingConflictException {
        String query = """
                SELECT b.id
                FROM main.bookings b
                JOIN relations.booking_time_slots bts ON b.id = bts.booking_id
                WHERE b.coworking_id = ? AND b.date = ? AND bts.time_slot_id IN (
                """;

        String placeholders = String.join(",", Collections.nCopies(updatedBooking.timeSlots().size(), "?"));
        query += placeholders + ")";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, updatedBooking.coworkingId());
            stmt.setDate(2, Date.valueOf(updatedBooking.date()));

            int index = 3;
            for (TimeSlot slot : updatedBooking.timeSlots()) {
                stmt.setLong(index++, slot.getId());
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long existingBookingId = rs.getLong("id");
                if (existingBookingId != updatedBooking.id()) {
                    throw new BookingConflictException("Время бронирования совпадает с существующим бронированием");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при проверке пересечения бронирований: " + e.getMessage());
        }
    }


    /**
     * Преобразует запрос к базе данных в бронирования
     *
     * @param statement - предварительно скомпилированный SQL-запрос
     */
    private Collection<Booking> statementToBookings(PreparedStatement statement) throws SQLException {
        Map<Long, Booking> bookingMap = new HashMap<>();
        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                long bookingId = rs.getLong("id");
                long userId = rs.getLong("user_id");
                long coworkingId = rs.getLong("coworking_id");
                LocalDate date = rs.getDate("date").toLocalDate();
                long timeSlotId = rs.getLong("time_slot_id");

                Booking booking = bookingMap.computeIfAbsent(bookingId, id ->
                        new Booking(
                                id,
                                userId,
                                coworkingId,
                                date,
                                new ArrayList<>()
                        )
                );

                TimeSlot timeSlot = getTimeSlotById(timeSlotId);
                booking.timeSlots().add(timeSlot);

            }
            return bookingMap.values();
        }
    }

    /**
     * Получение временного слота по ID
     *
     * @param id - ID временного слота
     */
    private TimeSlot getTimeSlotById(long id) {
        for (TimeSlot slot : TimeSlot.values()) {
            if (slot.getId() == id) {
                return slot;
            }
        }
        throw new IllegalArgumentException("Неизвестный TimeSlot ID: " + id);
    }

    /**
     * Вставляет время бронирования в связанную таблицу
     *
     * @param bookingId - ID бронирования
     * @param booking   - объект бронирования содержащий временные слоты
     */
    private void insertTimeSlots(long bookingId, Booking booking) throws SQLException {
        String insertTimeSlotSQL = """
                INSERT INTO relations.booking_time_slots (booking_id, time_slot_id) VALUES (?, ?)
                """;
        try (PreparedStatement timeSlotStatement = connection.prepareStatement(insertTimeSlotSQL)) {
            for (TimeSlot timeSlot : booking.timeSlots()) {
                timeSlotStatement.setLong(1, bookingId);
                timeSlotStatement.setLong(2, timeSlot.getId());
                timeSlotStatement.addBatch();
            }
            timeSlotStatement.executeBatch();
        }
    }

    /**
     * Удаляет время бронирования из связанной таблицы
     *
     * @param bookingId - ID бронирования
     */
    private void deleteTimeSlots(long bookingId) throws SQLException {
        String deleteTimeSlotsSQL = """
                DELETE FROM relations.booking_time_slots
                WHERE booking_id = ?
                """;
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteTimeSlotsSQL)) {
            deleteStatement.setLong(1, bookingId);
            deleteStatement.executeUpdate();
        }
    }
}
