package rest.repositories;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserRepositoryCollectionTest {

    @InjectMocks
    private UserRepositoryCollection userRepository;
    
    private RegistrationDTO testRegistrationDTO;

    @BeforeEach
    void setUp() {
        testRegistrationDTO = new RegistrationDTO("Test", "User", "test@example.com", "password");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() throws UserAlreadyExistsException {
        UserDTO userDTO = userRepository.createUser(testRegistrationDTO);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.email()).isEqualTo("test@example.com");
    }

    @Test
    void testCreateUserThrowsException() throws UserAlreadyExistsException {
        userRepository.createUser(testRegistrationDTO);
        assertThrows(UserAlreadyExistsException.class, () -> userRepository.createUser(testRegistrationDTO));
    }

    @Test
    void testFindByEmail() throws UserAlreadyExistsException {
        userRepository.createUser(testRegistrationDTO);

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
    void testFindById() throws UserAlreadyExistsException {
        UserDTO userDTO = userRepository.createUser(testRegistrationDTO);

        Optional<User> foundUser = userRepository.findById(userDTO.id());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().id()).isEqualTo(userDTO.id());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<User> foundUser = userRepository.findById(999L);
        assertThat(foundUser).isEmpty();
    }

    @Test
    void testDeleteUserById() throws UserNotFoundException, UserAlreadyExistsException {
        UserDTO userDTO = userRepository.createUser(testRegistrationDTO);

        userRepository.deleteUserById(userDTO.id());

        Optional<User> foundUser = userRepository.findById(userDTO.id());
        assertThat(foundUser).isEmpty();
    }

    @Test
    void testDeleteUserByIdThrowsException() {
        assertThrows(UserNotFoundException.class, () -> userRepository.deleteUserById(999L));
    }

    @Test
    void testUpdateUser() throws UserNotFoundException, UserAlreadyExistsException {
        UserDTO userDTO = userRepository.createUser(testRegistrationDTO);
        User updatedUser = new User(userDTO.id(), "UpdatedFirstName", "UpdatedLastName", "updated@example.com", "password");

        userRepository.updateUser(updatedUser);

        Optional<User> foundUser = userRepository.findById(userDTO.id());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().firstName()).isEqualTo("UpdatedFirstName");
        assertThat(foundUser.get().email()).isEqualTo("updated@example.com");
    }

    @Test
    void testUpdateUserThrowsUserNotFoundException() {
        User updatedUser = new User(999L, "UpdatedFirstName", "UpdatedLastName", "updated@example.com", "password");
        assertThrows(UserNotFoundException.class, () -> userRepository.updateUser(updatedUser));
    }

    @Test
    void testUpdateUserThrowsUserAlreadyExistsException() throws UserAlreadyExistsException {
        RegistrationDTO registrationDTO1 = new RegistrationDTO("Test", "User", "test@example.com", "password");
        RegistrationDTO registrationDTO2 = new RegistrationDTO("Test", "User", "test2@example.com", "password");
        userRepository.createUser(registrationDTO1);
        UserDTO userDTO2 = userRepository.createUser(registrationDTO2);
        User updatedUser = new User(userDTO2.id(), "Test", "User", "test@example.com", "password");

        assertThrows(UserAlreadyExistsException.class, () -> userRepository.updateUser(updatedUser));
    }

    @Test
    void testGetAllUsers() throws UserAlreadyExistsException {
        RegistrationDTO registrationDTO1 = new RegistrationDTO("test1@example.com", "password", "Test1", "User1");
        RegistrationDTO registrationDTO2 = new RegistrationDTO("test2@example.com", "password", "Test2", "User2");
        userRepository.createUser(registrationDTO1);
        userRepository.createUser(registrationDTO2);

        Collection<User> users = userRepository.getAllUsers();

        assertThat(users).hasSize(2);
    }
}
