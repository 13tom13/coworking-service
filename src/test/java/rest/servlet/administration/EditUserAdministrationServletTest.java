package rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.rest.servlet.administration.EditUserAdministrationServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("Тестирование сервлета для редактирования пользователя администратором")
public class EditUserAdministrationServletTest extends AdministrationServletTest {

    @InjectMocks
    private EditUserAdministrationServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksAdminServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное выполнение редактирования пользователя")
    public void testDoPostSuccess() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(UserDTOJson)));

        when(administrationService.editUserByAdministrator(userDTO)).thenReturn(userDTO);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String actualResponse = responseWriter.toString();
        String expectedResponse = String.format("\"Пользователь с именем: %s %s и email: %s успешно изменен\"", firstName,lastName,email);
        assertEquals(expectedResponse, actualResponse);
    }
}