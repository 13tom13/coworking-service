package rest.servlet.registration;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import io.ylab.tom13.coworkingservice.out.rest.servlet.registration.RegistrationServlet;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование сервлета регистрации")
public class RegistrationServletTest extends ServletTest {

    @InjectMocks
    private RegistrationServlet servlet;

    @Mock
    private RegistrationService registrationService;

    @BeforeEach
    public void setService() throws Exception {
        Field registrationServiceField = RegistrationServlet.class.getDeclaredField("registrationService");
        registrationServiceField.setAccessible(true);
        registrationServiceField.set(servlet, registrationService);
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void testDoPostSuccess() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(registrationDTOJson)));

        when(registrationService.createUser(any(RegistrationDTO.class))).thenReturn(userDTO);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String expectedResponse = String.format("\"Пользователь с именем: %s %s и email: %s успешно зарегистрирован\"",
                firstName, lastName, email);
        String actualResponse = responseWriter.toString().trim();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Пользователь уже существует")
    public void testDoPostUserAlreadyExists() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(registrationDTOJson)));

        when(registrationService.createUser(any(RegistrationDTO.class))).thenThrow(new UserAlreadyExistsException(email));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_CONFLICT, "Пользователь с email " + email + " уже существует");
    }

    @Test
    @DisplayName("Ошибка репозитория при регистрации")
    public void testDoPostRepositoryException() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(registrationDTOJson)));

        when(registrationService.createUser(any(RegistrationDTO.class))).thenThrow(new RepositoryException(repositoryException));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка репозитория: " + repositoryException);
    }
}
