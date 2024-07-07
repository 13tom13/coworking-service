package rest.servlet;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.rest.servlet.administration.EditUserAdministrationServlet;
import io.ylab.tom13.coworkingservice.out.utils.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import security.SecurityHTTPControllerTest;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class EditUserAdministrationServletTest extends SecurityHTTPControllerTest {

    @InjectMocks
    private EditUserAdministrationServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        responseWriter = new StringWriter();

        UserDTO userDTO = new UserDTO(3L, "John", "Doe", "john.doe@example.com", Role.ADMINISTRATOR);
        Optional<User> user = Optional.ofNullable(UserMapper.INSTANCE.toUser(userDTO));

        when(session.getUser()).thenReturn(Optional.of(userDTO));
        lenient().doReturn(user).when(userRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Успешное выполнение метода doPost")
    public void testDoPostSuccess() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(
                "{\"id\":3,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"role\":\"ADMINISTRATOR\"}")));

        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));


        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String actualResponse = responseWriter.toString();
        String expectedResponse = "\"Пользователь с именем: John Doe и email: john.doe@example.com успешно изменен\"";
        assertEquals(expectedResponse, actualResponse);
    }
}