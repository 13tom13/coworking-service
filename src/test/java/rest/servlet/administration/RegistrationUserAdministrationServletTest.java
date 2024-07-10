package rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.rest.servlet.administration.RegistrationUserAdministrationServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.AdministrationServletTest;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Тестирование сервлета для регистрации пользователя администратором")
public class RegistrationUserAdministrationServletTest extends AdministrationServletTest {

    @InjectMocks
    private RegistrationUserAdministrationServlet servlet;

    @BeforeEach
    public void setService() throws NoSuchFieldException, IllegalAccessException {
        injectMocksAdminServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void testDoPostSuccess() throws Exception {
        String expectedResponse = String.format("\"Пользователь с именем: %s %s и email: %s успешно зарегистрирован\"",
                firstName, lastName, email);

        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(registrationDTOJson)));

        servlet.service(request, response);

        verify(administrationService).registrationUser(any(RegistrationDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String actualResponse = responseWriter.toString();
        assertEquals(expectedResponse, actualResponse);
    }
}
