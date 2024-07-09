package rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.rest.servlet.administration.GetAllUsersServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.AdministrationServletTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование сервлета для получения всех пользователей администратором")
public class GetAllUsersServletTest extends AdministrationServletTest {

    @InjectMocks
    private GetAllUsersServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksAdminServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное получение всех пользователей")
    public void testDoGetSuccess() throws Exception {
        when(request.getMethod()).thenReturn(GET_METHOD);
        List<UserDTO> allUsers = Collections.singletonList(userDTO);
        when(administrationService.getAllUsers()).thenReturn(allUsers);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String actualResponse = responseWriter.toString();
        String expectedResponse = objectMapper.writeValueAsString(allUsers);
        System.out.println(expectedResponse);
        assertEquals(expectedResponse, actualResponse);
    }

}
