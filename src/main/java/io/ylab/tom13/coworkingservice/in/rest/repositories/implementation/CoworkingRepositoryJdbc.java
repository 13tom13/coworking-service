package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Реализация интерфейса {@link CoworkingRepository}.
 * CRUD операции с пользователями с использованием JDBC.
 */
public class CoworkingRepositoryJdbc implements CoworkingRepository {

    private final Connection connection;

    public CoworkingRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Coworking> getAllCoworking() throws RepositoryException {
        String sql = """
                SELECT id, name, description, available, type, workplace_type, conference_room_capacity
                FROM main.coworkings
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            Collection<Coworking> coworkings = new ArrayList<>();
            while (resultSet.next()) {
                coworkings.add(getCoworkingFromResultSet(resultSet));
            }
            return coworkings;
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при получении всех коворкингов: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Coworking> getCoworkingById(long coworkingId) throws RepositoryException {
        String sql = """
                SELECT id, name, description, available, type, workplace_type, conference_room_capacity
                FROM main.coworkings
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, coworkingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getCoworkingFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при получении коворкинга по ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Coworking> createCoworking(Coworking coworking) throws CoworkingConflictException, RepositoryException {
        if (!isCoworkingNameUnique(coworking.getName())) {
            throw new CoworkingConflictException("Имя коворкинга уже занято");
        }
        String sql = """
                INSERT INTO main.coworkings (name, description, available, type, workplace_type, conference_room_capacity)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                coworkingToStatement(coworking, statement);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    connection.rollback();
                    throw new RepositoryException("Создание коворкинга не удалось, нет затронутых строк.");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        connection.commit();
                        return getCoworkingById(id);
                    } else {
                        connection.rollback();
                        throw new RepositoryException("Создание коворкинга не удалось, ID не был получен.");
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                if (e.getSQLState().equals("23505")) { // unique_violation
                    throw new CoworkingConflictException("Имя коворкинга уже занято");
                } else {
                    throw new RepositoryException("Ошибка при создании коворкинга: " + e.getMessage());
                }
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при соединении с базой данных: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCoworking(long coworkingId) throws CoworkingNotFoundException, RepositoryException {
        String sql = """
                DELETE FROM main.coworkings
                WHERE id = ?
                """;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, coworkingId);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    connection.rollback();
                    throw new CoworkingNotFoundException("Коворкинг с указанным ID не найден");
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RepositoryException("Ошибка при удалении коворкинга: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка подключения: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Coworking> updateCoworking(Coworking coworking) throws CoworkingConflictException, CoworkingNotFoundException, RepositoryException {
        String sql = """
                UPDATE main.coworkings
                SET name = ?, description = ?, available = ?, type = ?, workplace_type = ?, conference_room_capacity = ?
                WHERE id = ?
                """;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                Coworking existingCoworking = getCoworkingById(coworking.getId()).orElseThrow(() ->
                        new CoworkingNotFoundException("Коворкинг с указанным ID не найден"));

                if (!existingCoworking.getName().equals(coworking.getName()) &&
                    !isCoworkingNameUnique(coworking.getName())) {
                    throw new CoworkingConflictException("Имя коворкинга уже занято");
                }

                coworkingToStatement(coworking, statement);
                statement.setLong(7, coworking.getId());

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    connection.rollback();
                    throw new RepositoryException("Обновление коворкинга не удалось, нет затронутых строк.");
                }

                connection.commit();
                return getCoworkingById(coworking.getId());
            } catch (SQLException e) {
                connection.rollback();
                if (e.getSQLState().equals("23505")) { // unique_violation
                    throw new CoworkingConflictException("Имя коворкинга уже занято");
                } else {
                    throw new RepositoryException("Ошибка при обновлении коворкинга: " + e.getMessage());
                }
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при соединении с базой данных: " + e.getMessage());
        }
    }

    /**
     * Проверяет уникальность имени коворкинга
     *
     * @param name имя коворкинга для проверки
     */
    private boolean isCoworkingNameUnique(String name) throws RepositoryException {
        String sql = """
                SELECT COUNT(*)
                FROM main.coworkings
                WHERE name = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
                return false;
            }
        } catch (SQLException e

        ) {
            throw new RepositoryException("Ошибка при проверке уникальности имени коворкинга: " + e.getMessage());
        }
    }


    /**
     * Получает объект {@link Coworking} из ResultSet
     *
     * @param resultSet результат выполнения запроса к базе данных
     */
    private Coworking getCoworkingFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        boolean available = resultSet.getBoolean("available");
        String type = resultSet.getString("type");
        String workplaceType = resultSet.getString("workplace_type");
        int conferenceRoomCapacity = resultSet.getInt("conference_room_capacity");

        if (Workplace.class.getSimpleName().equals(type)) {
            return new Workplace(id, name, description, available, workplaceType);
        } else if (ConferenceRoom.class.getSimpleName().equals(type)) {
            return new ConferenceRoom(id, name, description, available, conferenceRoomCapacity);
        } else {
            throw new SQLException("Неизвестный тип коворкинга в базе данных: " + type);
        }
    }

    private void coworkingToStatement(Coworking coworking, PreparedStatement statement) throws SQLException, RepositoryException {
        statement.setString(1, coworking.getName());
        statement.setString(2, coworking.getDescription());
        statement.setBoolean(3, coworking.isAvailable());
        statement.setString(4, coworking.getClass().getSimpleName());
        if (coworking instanceof Workplace) {
            statement.setString(5, ((Workplace) coworking).getType());
            statement.setNull(6, Types.INTEGER);
        } else if (coworking instanceof ConferenceRoom) {
            statement.setNull(5, Types.VARCHAR);
            statement.setInt(6, ((ConferenceRoom) coworking).getCapacity());
        } else {
            throw new RepositoryException("Неизвестный тип коворкинга");
        }
    }
}
