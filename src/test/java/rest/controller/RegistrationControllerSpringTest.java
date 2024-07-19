package rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.rest.controller.RegistrationControllerSpring;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.MvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Тесты контроллера регистрации пользователя")
class RegistrationControllerSpringTest extends MvcTest {

    private static final String REGISTRATION_URL = "/registration";

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationControllerSpring registrationController;

    private RegistrationDTO registrationDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        registrationDTO = new RegistrationDTO("John", "Doe", "john.doe@example.com", "password", Role.USER);
        userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com", Role.USER);
    }

    @Test
    @DisplayName("Тест успешного создания пользователя")
    void testCreateUserSuccess() throws Exception {
        when(registrationService.createUser(any(RegistrationDTO.class))).thenReturn(userDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(REGISTRATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        UserDTO responseUser = objectMapper.readValue(responseContent, UserDTO.class);

        assertThat(responseUser).isEqualTo(userDTO);
    }

    @Test
    @DisplayName("Тест попытки создания пользователя с существующим email")
    void testCreateUserUserAlreadyExistsException() throws Exception {
        String errorMessage = "User already exists";
        when(registrationService.createUser(any(RegistrationDTO.class))).thenThrow(new UserAlreadyExistsException(errorMessage));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(REGISTRATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).contains(errorMessage);
    }

    @Test
    @DisplayName("Тест ошибки сервера при создании пользователя")
    void testCreateUserRepositoryException() throws Exception {
        String errorMessage = "Internal server error";
        when(registrationService.createUser(any(RegistrationDTO.class))).thenThrow(new RepositoryException(errorMessage));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(REGISTRATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).contains(errorMessage);
    }
}
