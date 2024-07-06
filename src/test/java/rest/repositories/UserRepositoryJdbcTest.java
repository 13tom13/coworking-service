package rest.repositories;

import database.TestcontainersConnector;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryJdbc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@DisplayName("Тесты репозитория пользователей")
class UserRepositoryJdbcTest extends TestcontainersConnector {

    private UserRepositoryJdbc userRepository;

    private User user;


    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryJdbc(getTestConnection());
        user = new User(1L, "John", "Doe", "john.new@example.com", "password", Role.USER);
    }

    @Test
    @DisplayName("Тест создание пользователя")
    void createUser_ShouldCreateUser_WhenValidUserProvided() throws RepositoryException, UserAlreadyExistsException {
        Optional<User> createdUser = userRepository.createUser(user);

        Assertions.assertThat(createdUser).isPresent();
        Assertions.assertThat(createdUser.get().id()).isGreaterThan(0);
        Assertions.assertThat(createdUser.get().email()).isEqualTo(user.email());
    }

    @Test
    @DisplayName("Тест нахождения пользователя по email")
    void findByEmail_ShouldReturnUser_WhenUserExists() throws RepositoryException, UserAlreadyExistsException {
        userRepository.createUser(user);

        Optional<User> foundUser = userRepository.findByEmail(user.email());

        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().email()).isEqualTo(user.email());
    }

    @Test
    @DisplayName("Тест нахождения пользователя по ID")
    void findById_ShouldReturnUser_WhenUserExists() throws RepositoryException, UserAlreadyExistsException {
        User createdUser = userRepository.createUser(user).get();
        Optional<User> foundUser = userRepository.findById(createdUser.id());

        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().id()).isEqualTo(createdUser.id());
    }

    @Test
    @DisplayName("Тест удаление пользователя")
    void deleteUserById_ShouldDeleteUser_WhenUserExists() throws RepositoryException, UserNotFoundException, UserAlreadyExistsException {
        User createdUser = userRepository.createUser(user).get();

        userRepository.deleteUserById(createdUser.id());

        Optional<User> deletedUser = userRepository.findById(createdUser.id());
        Assertions.assertThat(deletedUser).isNotPresent();
    }

    @Test
    @DisplayName("Тест изменения пользователя")
    void updateUser_ShouldUpdateUser_WhenUserExists() throws RepositoryException, UserAlreadyExistsException, UserNotFoundException {
        User createdUser = userRepository.createUser(user).get();

        User updatedUser = new User(createdUser.id(), "Markus", "Doe", "markus.doe@example.com", "newpassword", Role.ADMINISTRATOR);
        Optional<User> result = userRepository.updateUser(updatedUser);

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().email()).isEqualTo("markus.doe@example.com");
        Assertions.assertThat(result.get().role()).isEqualTo(Role.ADMINISTRATOR);
    }
}
