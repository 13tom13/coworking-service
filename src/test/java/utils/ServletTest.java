package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static io.ylab.tom13.coworkingservice.out.config.ApplicationConfig.getObjectMapper;
import static org.mockito.Mockito.lenient;

@DisplayName("Создание заглушек для тестовых запросов и другие тестовые данные")
public class ServletTest extends SecurityHTTPControllerTest {

    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    protected StringWriter responseWriter;

    protected ObjectMapper objectMapper = getObjectMapper();


    protected static String POST_METHOD = "POST";
    protected String GET_METHOD  =  "GET";

    protected long id = 10L;
    protected String firstName = "John";
    protected String lastName = "Doe";
    protected String email  =  "john.doe@example.com";
    protected String password = "password";
    protected Role role  =  Role.USER;

    protected UserDTO userDTO = new UserDTO(id, firstName, lastName, email, Role.USER);

    protected String UserDTOJson = String.format("{\"id\":%s,\"firstName\":\"%s\",\"lastName\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"}",
            id,firstName,lastName,email, role);

    protected String registrationDTOJson = String.format("{\"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"role\": \"%s\"}",
            firstName, lastName, email, password, role);

    @BeforeEach
    public void setWriter() throws IOException {
        responseWriter = new StringWriter();
        lenient().when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }
}
