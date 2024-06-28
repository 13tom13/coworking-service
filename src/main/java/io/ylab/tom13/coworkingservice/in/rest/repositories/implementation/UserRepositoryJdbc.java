//package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;
//
//import io.ylab.tom13.coworkingservice.in.database.DatabaseConnection;
//import io.ylab.tom13.coworkingservice.in.entity.model.User;
//import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
//import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
//import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Optional;
//
///**
// * Реализация репозитория пользователей с использованием JDBC.
// */
//public class UserRepositoryJdbc implements UserRepository {
//
//    private static final String INSERT_USER_SQL = "INSERT INTO users (first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, ?)";
//    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM users";
//    private static final String SELECT_USER_BY_EMAIL_SQL = "SELECT * FROM users WHERE email = ?";
//    private static final String SELECT_USER_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
//    private static final String DELETE_USER_BY_ID_SQL = "DELETE FROM users WHERE id = ?";
//    private static final String UPDATE_USER_SQL = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ?, role = ? WHERE id = ?";
//
//    @Override
//    public Optional<User> createUser(User user) throws RepositoryException {
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
//            preparedStatement.setString(1, user.firstName());
//            preparedStatement.setString(2, user.lastName());
//            preparedStatement.setString(3, user.email());
//            preparedStatement.setString(4, user.password());
//            preparedStatement.setString(5, user.role());
//
//            int affectedRows = preparedStatement.executeUpdate();
//            if (affectedRows == 0) {
//                throw new RepositoryException("Creating user failed, no rows affected.");
//            }
//
//            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    long id = generatedKeys.getLong(1);
//                    return Optional.of(new User(id, user.firstName(), user.lastName(), user.email(), user.password(), user.role()));
//                } else {
//                    throw new RepositoryException("Creating user failed, no ID obtained.");
//                }
//            }
//        } catch (SQLException e) {
//            throw new RepositoryException("Error creating user", e);
//        }
//    }
//
//    @Override
//    public Collection<User> getAllUsers() throws RepositoryException {
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_SQL);
//             ResultSet resultSet = preparedStatement.executeQuery()) {
//
//            Collection<User> users = new ArrayList<>();
//            while (resultSet.next()) {
//                long id = resultSet.getLong("id");
//                String firstName = resultSet.getString("first_name");
//                String lastName = resultSet.getString("last_name");
//                String email = resultSet.getString("email");
//                String password = resultSet.getString("password");
//                String role = resultSet.getString("role");
//                users.add(new User(id, firstName, lastName, email, password, role));
//            }
//            return users;
//        } catch (SQLException e) {
//            throw new RepositoryException("Error fetching all users", e);
//        }
//    }
//
//    @Override
//    public Optional<User> findByEmail(String email) throws RepositoryException {
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL_SQL)) {
//            preparedStatement.setString(1, email);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    long id = resultSet.getLong("id");
//                    String firstName = resultSet.getString("first_name");
//                    String lastName = resultSet.getString("last_name");
//                    String password = resultSet.getString("password");
//                    String role = resultSet.getString("role");
//                    return Optional.of(new User(id, firstName, lastName, email, password, role));
//                }
//            }
//        } catch (SQLException e) {
//            throw new RepositoryException("Error finding user by email", e);
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    public Optional<User> findById(long id) throws RepositoryException {
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID_SQL)) {
//            preparedStatement.setLong(1, id);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    String firstName = resultSet.getString("first_name");
//                    String lastName = resultSet.getString("last_name");
//                    String email = resultSet.getString("email");
//                    String password = resultSet.getString("password");
//                    String role = resultSet.getString("role");
//                    return Optional.of(new User(id, firstName, lastName, email, password, role));
//                }
//            }
//        } catch (SQLException e) {
//            throw new RepositoryException("Error finding user by ID", e);
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    public void deleteUserById(long id) throws UserNotFoundException, RepositoryException {
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID_SQL)) {
//            preparedStatement.setLong(1, id);
//            int affectedRows = preparedStatement.executeUpdate();
//            if (affectedRows == 0) {
//                throw new UserNotFoundException("User with ID " + id + " not found.");
//            }
//        } catch (SQLException e) {
//            throw new RepositoryException("Error deleting user by ID", e);
//        }
//    }
//
//    @Override
//    public Optional<User> updateUser(User user) throws RepositoryException {
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {
//            preparedStatement.setString(1, user.firstName());
//            preparedStatement.setString(2, user.lastName());
//            preparedStatement.setString(3, user.email());
//            preparedStatement.setString(4, user.password());
//            preparedStatement.setString(5, user.role());
//            preparedStatement.setLong(6, user.id());
//
//            int affectedRows = preparedStatement.executeUpdate();
//            if (affectedRows == 0) {
//                throw new RepositoryException("Updating user failed, no rows affected.");
//            }
//
//            return Optional.of(user);
//        } catch (SQLException e) {
//            throw new RepositoryException("Error updating user", e);
//        }
//    }
//}
