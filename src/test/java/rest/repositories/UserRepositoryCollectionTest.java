package rest.repositories;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserRepositoryCollectionTest {

    @InjectMocks
    private UserRepositoryCollection userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(userRepository.getNewId(), "Test", "User", "test@example.com", "password", Role.USER);
    }

    @Test
    void testCreateUser() throws RepositoryException {
        User createdUser = userRepository.createUser(testUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.email()).isEqualTo("test@example.com");
    }

    @Test
    void testCreateUserThrowsException() throws RepositoryException {
        userRepository.createUser(testUser);
        assertThrows(RepositoryException.class, () -> userRepository.createUser(testUser));
    }

    @Test
    void testFindByEmail() throws RepositoryException {
        userRepository.createUser(testUser);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().email()).isEqualTo("test@example.com");
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<User> foundUser = userRepository.findByEmail("notfound@example.com");
        assertThat(foundUser).isEmpty();
    }

    @Test
    void testFindById() throws RepositoryException {
        User createdUser = userRepository.createUser(testUser);

        Optional<User> foundUser = userRepository.findById(createdUser.id());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().id()).isEqualTo(createdUser.id());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<User> foundUser = userRepository.findById(999L);
        assertThat(foundUser).isEmpty();
    }

    @Test
    void testDeleteUserById() throws RepositoryException, UserNotFoundException {
        User createdUser = userRepository.createUser(testUser);

        userRepository.deleteUserById(createdUser.id());

        Optional<User> foundUser = userRepository.findById(createdUser.id());
        assertThat(foundUser).isEmpty();
    }

    @Test
    void testDeleteUserByIdThrowsException() {
        assertThrows(UserNotFoundException.class, () -> userRepository.deleteUserById(999L));
    }

    @Test
    void testUpdateUser() throws RepositoryException {
        User createdUser = userRepository.createUser(testUser);
        User updatedUser = new User(createdUser.id(), "UpdatedFirstName", "UpdatedLastName", "updated@example.com", "password", Role.USER);

        userRepository.updateUser(updatedUser);

        Optional<User> foundUser = userRepository.findById(createdUser.id());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().firstName()).isEqualTo("UpdatedFirstName");
        assertThat(foundUser.get().email()).isEqualTo("updated@example.com");
    }

    @Test
    void testGetAllUsers() throws RepositoryException {
        User user1 = new User(userRepository.getNewId(), "Test1", "User1", "test1@example.com", "password1", Role.USER);
        User user2 = new User(userRepository.getNewId(), "Test2", "User2", "test2@example.com", "password2", Role.USER);

        userRepository.createUser(user1);
        userRepository.createUser(user2);

        Collection<User> users = userRepository.getAllUsers();

        assertThat(users).hasSize(2);
    }
}
