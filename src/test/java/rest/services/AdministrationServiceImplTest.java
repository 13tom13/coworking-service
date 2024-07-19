package rest.services;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.AdministrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.MvcTest;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса администратора")
class AdministrationServiceImplTest extends MvcTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdministrationServiceImpl administrationService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field administrationServiceImpl = AdministrationServiceImpl.class.getDeclaredField("userRepository");
        administrationServiceImpl.setAccessible(true);
        administrationServiceImpl.set(administrationService, userRepository);
    }

    @Test
    @DisplayName("Успешное получение списка всех пользователей")
    void testGetAllUsersSuccess() {
        assertThat(administrationService.getAllUsers()).isEmpty();
        verify(userRepository, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void testGetUserByEmailFound() throws UserNotFoundException {
        String email = "user@example.com";
        User user = new User(1L, "John", "Doe", email, "password", Role.USER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThat(administrationService.getUserByEmail(email)).isNotNull();
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Пользователь не найден")
    void testGetUserByEmailNotFound() {
        String email = "nonexistent.user@example.com";
        assertThrows(UserNotFoundException.class, () -> administrationService.getUserByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Успешное редактирование пользователя")
    void testEditUserByAdministratorSuccess() throws UserNotFoundException, RepositoryException, UserAlreadyExistsException {
        UserDTO userDTO = new UserDTO(2L, "Edited", "User", "edited.user@example.com", Role.USER);
        User user = new User(userDTO.id(), userDTO.firstName(), userDTO.lastName(), userDTO.email(), "password", Role.USER);

        when(userRepository.findById(userDTO.id())).thenReturn(Optional.of(user));
        when(userRepository.updateUser(user)).thenReturn(Optional.of(user));

        UserDTO userDTOFromRep = administrationService.editUserByAdministrator(userDTO);

        assertThat(userDTOFromRep).isEqualTo(userDTO);
        verify(userRepository, times(1)).findById(userDTO.id());
        verify(userRepository, times(1)).updateUser(any());
    }

    @Test
    @DisplayName("Пользователь для изменений не найден")
    void testEditUserByAdministratorUserNotFound() throws RepositoryException, UserNotFoundException, UserAlreadyExistsException {
        UserDTO userDTO = new UserDTO(999L, "Nonexistent", "User", "nonexistent.user@example.com", Role.USER);
        assertThrows(UserNotFoundException.class, () -> administrationService.editUserByAdministrator(userDTO));
        verify(userRepository, times(1)).findById(userDTO.id());
        verify(userRepository, never()).updateUser(any());
    }
}
