package rest.services;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.AuthorizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static io.ylab.tom13.coworkingservice.out.security.PasswordUtil.hashPassword;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса авторизации")
public class AuthorizationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    private User existingUser;

    private final String password = "password";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        existingUser = new User(1L, "John", "Doe", "john.doe@example.com", hashPassword(password), Role.USER);

        Field userRepositoryField = AuthorizationServiceImpl.class.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(authorizationService, userRepository);
    }

    @Test
    @DisplayName("Тест успешной авторизации")
    void testLoginSuccess() throws UnauthorizedException {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO(existingUser.email(), password);

        when(userRepository.findByEmail(existingUser.email())).thenReturn(Optional.of(existingUser));

        UserDTO userDTO = authorizationService.login(authorizationDTO);

        assertEquals(existingUser.id(), userDTO.id());
        assertEquals(existingUser.firstName(), userDTO.firstName());
        assertEquals(existingUser.lastName(), userDTO.lastName());
        assertEquals(existingUser.email(), userDTO.email());
    }

    @Test
    @DisplayName("Тест ввода неверного пароля")
    void testLoginUnauthorized() {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO(existingUser.email(), "wrongPassword");

        when(userRepository.findByEmail(existingUser.email())).thenReturn(Optional.of(existingUser));

        assertThrows(UnauthorizedException.class, () -> authorizationService.login(authorizationDTO));
    }

    @Test
    @DisplayName("Тест ввода неверного адреса электронной почты")
    void testLoginUserNotFound() {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO("nonexistent.user@example.com", password);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authorizationService.login(authorizationDTO));
    }
}
