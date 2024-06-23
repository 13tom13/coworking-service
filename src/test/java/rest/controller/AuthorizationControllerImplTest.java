package rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.controller.user.implementation.AuthorizationControllerImpl;
import io.ylab.tom13.coworkingservice.in.rest.controller.user.implementation.RegistrationControllerImpl;
import io.ylab.tom13.coworkingservice.in.rest.services.user.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorizationControllerImplTest {

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private AuthorizationControllerImpl authorizationController;

    private AuthorizationDTO validAuthorizationDTO;
    private AuthorizationDTO invalidAuthorizationDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        validAuthorizationDTO = new AuthorizationDTO("john.doe@example.com", "password");
        invalidAuthorizationDTO = new AuthorizationDTO("john.doe@example.com", "wrongPassword");
        userDTO = new UserDTO(1L, "john.doe@example.com", "John", "Doe");

        Field authorizationControllerField = AuthorizationControllerImpl.class.getDeclaredField("authorizationService");
        authorizationControllerField.setAccessible(true);
        authorizationControllerField.set(authorizationController, authorizationService);
    }

    @Test
    void testLoginSuccess() throws UnauthorizedException {
        when(authorizationService.login(validAuthorizationDTO)).thenReturn(userDTO);

        ResponseDTO<UserDTO> response = authorizationController.login(validAuthorizationDTO);

        assertThat(response.data()).isEqualTo(userDTO);
        assertThat(response.message()).isNull();

        verify(authorizationService, times(1)).login(validAuthorizationDTO);
    }

    @Test
    void testLoginUnauthorizedException() throws UnauthorizedException {

        String errorMessage = "Неверный email или пароль";
        when(authorizationService.login(invalidAuthorizationDTO)).thenThrow(new UnauthorizedException());


        ResponseDTO<UserDTO> response = authorizationController.login(invalidAuthorizationDTO);

        assertThat(response.message()).contains(errorMessage);
        assertThat(response.data()).isNull();


        verify(authorizationService, times(1)).login(invalidAuthorizationDTO);
    }
}
