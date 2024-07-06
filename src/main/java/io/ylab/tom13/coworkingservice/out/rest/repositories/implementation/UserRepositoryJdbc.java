package io.ylab.tom13.coworkingservice.out.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * Реализация интерфейса {@link UserRepository}.
 * CRUD операции с пользователями с использованием JDBC.
 */
public class UserRepositoryJdbc implements UserRepository {

    private static final String UNIQUE_VIOLATION = "23505";

    private final Connection connection;

    public UserRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> createUser(User user) throws RepositoryException, UserAlreadyExistsException {
        String sql = """
                INSERT INTO main.users (first_name, last_name, email, password,role)
                VALUES (?, ?, ?, ?, ?)
                """;
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                userToStatement(user, statement);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    throw new RepositoryException("Создание пользователя не удалось, нет затронутых строк.");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        connection.commit();
                        return Optional.of(new User(id, user.firstName(), user.lastName(), user.email(), user.password(), user.role()));
                    } else {
                        connection.rollback();
                        throw new RepositoryException("Создание пользователя не удалось, ID не был получен.");
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                    throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
                } else {
                    throw new RepositoryException("Ошибка при создании пользователя: " + e.getMessage());
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
    public Collection<User> getAllUsers() {
        String sql = """
                SELECT *
                FROM main.users
                """;
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = resultSetToUser(resultSet);
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка пользователей" + e.getMessage());
            return users;
        }

        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT *
                FROM main.users WHERE email = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = resultSetToUser(resultSet);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске пользователя по email: " + e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findById(long id) {
        String sql = """
                SELECT *
                FROM main.users WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = resultSetToUser(resultSet);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения пользователя по ID: " + e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserById(long id) throws UserNotFoundException, RepositoryException {
        String sql = """
                DELETE FROM main.users
                WHERE id = ?
                """;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new UserNotFoundException("С ID " + id);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RepositoryException("Ошибка при удалении пользователя: " + e.getMessage());
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
    public Optional<User> updateUser(User user) throws RepositoryException, UserAlreadyExistsException, UserNotFoundException {
        String sql = """
                UPDATE main.users
                SET first_name = ?, last_name = ?, email = ?, password = ?, role = ?
                WHERE id = ?
                """;
        long userId  = user.id();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                userToStatement(user, preparedStatement);
                preparedStatement.setLong(6, userId);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new UserNotFoundException("с ID " + userId);
                }

                connection.commit();
                return findById(userId);
            } catch (SQLException e) {
                connection.rollback();
                if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                    throw new UserAlreadyExistsException(user.email());
                }
                throw new RepositoryException("Ошибка при обновлении пользователя: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка подключения: " + e.getMessage());
        }
    }

    /**
     * Метод для получения объекта {@link User} из {@link ResultSet}
     */
    private User resultSetToUser (ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }

    /**
     * Метод для передачи объекта {@link User} в {@link PreparedStatement}
     */
    private void userToStatement (User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.firstName());
        statement.setString(2, user.lastName());
        statement.setString(3, user.email());
        statement.setString(4, user.password());
        statement.setString(5, user.role().name());
    }
}
