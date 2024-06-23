package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.user.implementation.UserEditServiceImpl;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserEditServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEditServiceImpl userEditService;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEditUser() throws RepositoryException {
        UserDTO userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com");
        String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
        User userFromRepo = new User(1L, "John", "Doe", "john.doe@example.com", hashedPassword);

        UserDTO editedUserDTO = userEditService.editUser(userDTO);

        // Проверяем, что результат не равен null и соответствует ожиданиям
        assertThat(editedUserDTO).isNotNull();
        assertThat(editedUserDTO.id()).isEqualTo(userDTO.id());
        assertThat(editedUserDTO.firstName()).isEqualTo(userDTO.firstName());
        assertThat(editedUserDTO.lastName()).isEqualTo(userDTO.lastName());
        assertThat(editedUserDTO.email()).isEqualTo(userDTO.email());

        // Проверяем, что методы findById и updateUser были вызваны один раз
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).updateUser(any(User.class));
    }

    @Test
    void testEditUserThrowsUserNotFoundException() throws UserNotFoundException, UserAlreadyExistsException {
        UserDTO userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userEditService.editUser(userDTO));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).updateUser(any(User.class));
    }

    @Test
    void testEditPassword() throws UnauthorizedException, RepositoryException {
        String email = "john.doe@example.com";
        String oldPassword = "password";
        String newPassword = "newpassword";
        String hashedPassword = BCrypt.hashpw(oldPassword, BCrypt.gensalt());
        User userFromRepo = new User(1L, "John", "Doe", email, hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(userFromRepo));
        doNothing().when(userRepository).updateUser(any(User.class));

        userEditService.editPassword(email, oldPassword, newPassword);

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).updateUser(any(User.class));
    }

    @Test
    void testEditPasswordThrowsUserNotFoundException() throws UserNotFoundException, UserAlreadyExistsException {
        String email = "john.doe@example.com";
        String oldPassword = "password";
        String newPassword = "newpassword";

        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userEditService.editPassword(email, oldPassword, newPassword));
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).updateUser(any(User.class));
    }

    @Test
    void testEditPasswordThrowsUnauthorizedException() throws UserNotFoundException, UserAlreadyExistsException {
        String email = "john.doe@example.com";
        String oldPassword = "password";
        String incorrectPassword = "incorrectpassword";
        String hashedPassword = BCrypt.hashpw(oldPassword, BCrypt.gensalt());
        User userFromRepo = new User(1L, "John", "Doe", email, hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(userFromRepo));

        assertThrows(UnauthorizedException.class, () -> userEditService.editPassword(email, incorrectPassword, "newpassword"));
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).updateUser(any(User.class));
    }
}
