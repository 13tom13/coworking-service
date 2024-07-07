package rest.servlet;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
import io.ylab.tom13.coworkingservice.out.rest.servlet.authorization.AuthorizationServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.ServletTest;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тесты сервлета для авторизации")
public class AuthorizationServletTest extends ServletTest {

    @InjectMocks
    private AuthorizationServlet servlet;

    @Mock
    private AuthorizationService authorizationService;

    private AuthorizationDTO authorizationDTO;

    private String authorizationDTOJson;


    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        authorizationDTO = new AuthorizationDTO(email, password);
        authorizationDTOJson = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);

        Field authorizationServletField = AuthorizationServlet.class.getDeclaredField("authorizationService");
        authorizationServletField.setAccessible(true);
        authorizationServletField.set(servlet, authorizationService);
    }

    @Test
    @DisplayName("Успешное выполнение метода doPost для авторизации")
    public void testDoPostSuccess() throws Exception {
        String expectedResponse = String.format("\"Пользователь с именем: %s %s и email: %s успешно авторизирован\"",
                firstName, lastName, email);

        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(authorizationDTOJson)));
        when(authorizationService.login(authorizationDTO)).thenReturn(userDTO);

        servlet.service(request, response);

        String actualResponse = responseWriter.toString();
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Неудачное выполнение метода doPost для авторизации")
    public void testDoPostUnauthorized() throws Exception {
        String exceptionMessage = "Неверные учетные данные";

        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(authorizationDTOJson)));
        when(authorizationService.login(authorizationDTO)).thenThrow(new UnauthorizedException(exceptionMessage));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, exceptionMessage);
    }
}
