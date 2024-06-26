package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.user.implementation.AdministrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса администратора")
class AdministrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdministrationServiceImpl administrationService;

    private AuthenticationDTO adminDTO;

    private User admin;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field administrationServiceImpl = AdministrationServiceImpl.class.getDeclaredField("userRepository");
        administrationServiceImpl.setAccessible(true);
        administrationServiceImpl.set(administrationService, userRepository);
        admin = new User(1L, "John", "Doe", "admin@example.com", "password", Role.ADMINISTRATOR);
        adminDTO = new AuthenticationDTO(admin.id());
        when(userRepository.findById(adminDTO.userId())).thenReturn(Optional.ofNullable(admin));
    }

    @Test
    @DisplayName("Успешное получение списка всех пользователей")
    void testGetAllUsersSuccess() throws NoAccessException {
        assertThat(administrationService.getAllUsers(adminDTO)).isEmpty();
        verify(userRepository, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void testGetUserByEmailFound() throws UserNotFoundException, NoAccessException {
        String email = "user@example.com";
        User user = new User(1L, "John", "Doe", email, "password", Role.USER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThat(administrationService.getUserByEmail(adminDTO, email)).isNotNull();
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Пользователь не найден")
    void testGetUserByEmailNotFound() {
        String email = "nonexistent.user@example.com";
        assertThrows(UserNotFoundException.class, () -> administrationService.getUserByEmail(adminDTO, email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Успешное редактирование пользователя")
    void testEditUserByAdministratorSuccess() throws UserNotFoundException, NoAccessException, RepositoryException {
        UserDTO userDTO = new UserDTO(2L, "Edited", "User", "edited.user@example.com", Role.USER);
        User user  = new User(userDTO.id(),  userDTO.firstName(),  userDTO.lastName(),  userDTO.email(),  "password", Role.USER);

        when(userRepository.findById(userDTO.id())).thenReturn(Optional.of(user));
        when(userRepository.updateUser(user)).thenReturn(user);

        UserDTO userDTOFromRep = administrationService.editUserByAdministrator(adminDTO, userDTO);

        assertThat(userDTOFromRep).isEqualTo(userDTO);
        verify(userRepository, times(1)).findById(userDTO.id());
        verify(userRepository, times(1)).updateUser(any());
    }

    @Test
    @DisplayName("Пользователь для изменений не найден")
    void testEditUserByAdministratorUserNotFound() throws RepositoryException {
        UserDTO userDTO = new UserDTO(999L, "Nonexistent", "User", "nonexistent.user@example.com", Role.USER);
        assertThrows(UserNotFoundException.class, () -> administrationService.editUserByAdministrator(adminDTO, userDTO));
        verify(userRepository, times(1)).findById(userDTO.id());
        verify(userRepository, never()).updateUser(any());
    }
}
