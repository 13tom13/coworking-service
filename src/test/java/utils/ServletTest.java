package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

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


    protected static final String POST_METHOD = "POST";
    protected static final String GET_METHOD = "GET";

    protected final long userId = 999L;
    protected final String firstName = "John";
    protected final String lastName = "Doe";
    protected final String email = "john.doe@example.com";
    protected final String password = "password";
    protected final Role role = Role.USER;

    protected final long bookingId = 998L;
    protected final LocalDate date = LocalDate.now().plusDays(1);
    protected final List<TimeSlot> timeSlots = List.of(TimeSlot.MORNING, TimeSlot.AFTERNOON);

    protected final long coworkingId = 997L;
    protected final String coworkingName = "TestCoworking";
    protected final String coworkingDescription = "Coworking for testing";
    protected final boolean available = true;
    protected final String workplaceType = "test workplace";

    protected final String repositoryException = "Internal error";

    protected UserDTO userDTO;

    protected String userDTOJson;
    protected String registrationDTOJson;


    @BeforeEach
    public void setWriterAndUserDTO() throws IOException {
        userDTO = new UserDTO(userId, firstName, lastName, email, Role.USER);
        userDTOJson = objectMapper.writeValueAsString(userDTO);

        RegistrationDTO registrationDTO = new RegistrationDTO(firstName, lastName, email, password, role);
        registrationDTOJson = objectMapper.writeValueAsString(registrationDTO);


        responseWriter = new StringWriter();
        lenient().when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }
}
