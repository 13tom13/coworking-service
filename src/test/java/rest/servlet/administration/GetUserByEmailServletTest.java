package rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.administration.GetUserByEmailServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServiceServlet.EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование сервлета для получения пользователя по email администратором")
public class GetUserByEmailServletTest extends AdministrationServletTest {

    @InjectMocks
    private GetUserByEmailServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksAdminServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное получение пользователя по email")
    public void testDoGetSuccess() throws Exception {
        when(request.getMethod()).thenReturn(GET_METHOD);
        when(request.getParameter(EMAIL)).thenReturn(email);

        when(administrationService.getUserByEmail(email)).thenReturn(userDTO);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String actualResponse = responseWriter.toString();
        String expectedResponse = objectMapper.writeValueAsString(userDTO);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Пользователь не найден")
    public void testDoGetUserNotFound() throws Exception {
        String expectedResponse = String.format("Пользователь %s не найден", email);

        when(request.getMethod()).thenReturn(GET_METHOD);
        when(request.getParameter(EMAIL)).thenReturn(email);
        when(administrationService.getUserByEmail(email)).thenThrow(new UserNotFoundException(email));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, expectedResponse);
    }
}
