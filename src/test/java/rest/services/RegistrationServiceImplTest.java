package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса регистрации пользователя")
public class RegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private RegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field coworkingServiceField = RegistrationServiceImpl.class.getDeclaredField("userRepository");
        coworkingServiceField.setAccessible(true);
        coworkingServiceField.set(registrationService, userRepository);

        registrationDTO = new RegistrationDTO("John", "Doe", "john.doe@example.com", "password");
    }

    @Test
    @DisplayName("Тест создания пользователя")
    void testCreateUser() throws UserAlreadyExistsException, RepositoryException {
        User expectedUser = new User(0, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(), registrationDTO.password(), Role.USER);

        when(userRepository.createUser(any(User.class))).thenReturn(Optional.of(expectedUser));

        UserDTO actualUserDTO = registrationService.createUser(registrationDTO);

        assertEquals(expectedUser.firstName(), actualUserDTO.firstName());
        assertEquals(expectedUser.lastName(), actualUserDTO.lastName());
        assertEquals(expectedUser.email(), actualUserDTO.email());
        assertEquals(expectedUser.role(), actualUserDTO.role());
    }

    @Test
    @DisplayName("Тест ошибка репозитория при создании пользователя")
    void testExceptionRepository() throws RepositoryException {
        when(userRepository.createUser(any(User.class))).thenReturn(Optional.empty());

        assertThrows(RepositoryException.class, () -> registrationService.createUser(registrationDTO));
    }

    @Test
    @DisplayName("Тест сохранения с уже существующим email")
    void testAlreadyExistsException() {
        User user = new User(0, registrationDTO.firstName(),registrationDTO.lastName(),registrationDTO.email(),registrationDTO.password(), Role.USER);
        when(userRepository.findByEmail(registrationDTO.email())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> registrationService.createUser(registrationDTO));
    }
}
