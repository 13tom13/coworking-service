package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.AuthorizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    private User existingUser;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        existingUser = new User(1L, "John", "Doe", "john.doe@example.com", BCrypt.hashpw("password", BCrypt.gensalt()), Role.USER);

        Field userRepositoryField = AuthorizationServiceImpl.class.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(authorizationService, userRepository);
    }

    @Test
    void testLoginSuccess() throws UnauthorizedException {
        // Подготовка тестовых данных
        AuthorizationDTO authorizationDTO = new AuthorizationDTO("john.doe@example.com", "password");

        // Настройка моков и их поведение
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(existingUser));

        // Вызов метода на тестирование
        UserDTO userDTO = authorizationService.login(authorizationDTO);

        // Проверка результата
        assertEquals(existingUser.id(), userDTO.id());
        assertEquals(existingUser.firstName(), userDTO.firstName());
        assertEquals(existingUser.lastName(), userDTO.lastName());
        assertEquals(existingUser.email(), userDTO.email());
    }

    @Test
    void testLoginUnauthorized() {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO("john.doe@example.com", "wrongPassword");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(existingUser));

        assertThrows(UnauthorizedException.class, () -> authorizationService.login(authorizationDTO));
    }

    @Test
    void testLoginUserNotFound() {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO("nonexistent.user@example.com", "password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authorizationService.login(authorizationDTO));
    }
}
