package rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.rest.servlet.administration.EditUserPasswordAdministratorServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@DisplayName("Тестирование сервлета для редактирования пользователя администратором")
public class EditUserPasswordAdministratorServletTest extends AdministrationServletTest {

    @InjectMocks
    private EditUserPasswordAdministratorServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksAdminServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное изменение пароля пользователя")
    public void testDoPostSuccess() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getParameter("userId")).thenReturn(String.valueOf(id));
        when(request.getParameter("newPassword")).thenReturn(password);
        doNothing().when(administrationService).editUserPasswordByAdministrator(anyLong(), anyString());

        servlet.service(request, response);

        verify(administrationService).editUserPasswordByAdministrator(anyLong(), anyString());
        verify(response).setStatus(HttpServletResponse.SC_OK);
        String actualResponse = responseWriter.toString();
        String expectedResponse = String.format("\"Пароль пользователя с ID: %s успешно изменен\"", id);
        assertEquals(expectedResponse, actualResponse);
    }
}
