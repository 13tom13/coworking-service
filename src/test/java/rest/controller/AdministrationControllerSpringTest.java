package rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.controller.AdministrationControllerSpring;
import io.ylab.tom13.coworkingservice.out.rest.services.AdministrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.MvcTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты контроллера администрирования пользователя")
public class AdministrationControllerSpringTest extends MvcTest {

    private static final String ADMIN_USERS_URL = "/admin/users";
    private static final String ADMIN_USER_URL = "/admin/user";
    private static final String ADMIN_USER_EDIT_URL = "/admin/user/edit";
    private static final String ADMIN_USER_EDIT_PASSWORD_URL = "/admin/user/edit/password";
    private static final String ADMIN_USER_REGISTRATION_URL = "/admin/user/registration";

    @Mock
    private AdministrationService administrationService;

    @InjectMocks
    private AdministrationControllerSpring administrationController;


    private UserDTO userDTO;
    private PasswordChangeDTO passwordChangeDTO;
    private RegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(administrationController).build();
        objectMapper = new ObjectMapper();
        userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com", Role.USER);
        passwordChangeDTO = new PasswordChangeDTO("john.doe@example.com", "oldPassword", "newPassword");
        registrationDTO = new RegistrationDTO("John", "Doe", "john.doe@example.com", "password", Role.USER);
    }

    @Test
    @DisplayName("Тест получения всех пользователей")
    void testGetAllUsers() throws Exception {
        when(administrationService.getAllUsers()).thenReturn(Collections.singletonList(userDTO));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        UserDTO[] users = objectMapper.readValue(responseContent, UserDTO[].class);

        assertThat(users).containsExactly(userDTO);
    }

    @Test
    @DisplayName("Тест получения пользователя по email")
    void testGetUserByEmail() throws Exception {
        when(administrationService.getUserByEmail(anyString())).thenReturn(userDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_USER_URL)
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        UserDTO responseUser = objectMapper.readValue(responseContent, UserDTO.class);

        assertThat(responseUser).isEqualTo(userDTO);
    }

    @Test
    @DisplayName("Тест ошибки при получении пользователя по email")
    void testGetUserByEmailNotFound() throws Exception {
        String errorMessage = "User not found";
        when(administrationService.getUserByEmail(anyString())).thenThrow(new UserNotFoundException(errorMessage));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_USER_URL)
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).contains(errorMessage);
    }

    @Test
    @DisplayName("Тест успешного редактирования пользователя")
    void testEditUserByAdministratorSuccess() throws Exception {
        when(administrationService.editUserByAdministrator(any(UserDTO.class))).thenReturn(userDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch(ADMIN_USER_EDIT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        UserDTO responseUser = objectMapper.readValue(responseContent, UserDTO.class);

        assertThat(responseUser).isEqualTo(userDTO);
    }

    @Test
    @DisplayName("Тест ошибки при редактировании пользователя")
    void testEditUserByAdministratorNotFound() throws Exception {
        String errorMessage = "User not found";
        when(administrationService.editUserByAdministrator(any(UserDTO.class))).thenThrow(new UserNotFoundException(errorMessage));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch(ADMIN_USER_EDIT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).contains(errorMessage);
    }

    @Test
    @DisplayName("Тест успешного изменения пароля пользователя")
    void testEditUserPasswordByAdministratorSuccess() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch(ADMIN_USER_EDIT_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordChangeDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).isEqualTo("Пароль пользователя успешно изменен");
    }

    @Test
    @DisplayName("Тест ошибки при изменении пароля пользователя")
    void testEditUserPasswordByAdministratorNotFound() throws Exception {
        String errorMessage = "User not found";
        doThrow(new UserNotFoundException(errorMessage)).when(administrationService).editUserPasswordByAdministrator(anyString(), anyString());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch(ADMIN_USER_EDIT_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordChangeDTO)))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).contains(errorMessage);
    }

    @Test
    @DisplayName("Тест успешной регистрации пользователя")
    void testRegistrationUserSuccess() throws Exception {
        when(administrationService.registrationUser(any(RegistrationDTO.class))).thenReturn(userDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_USER_REGISTRATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        UserDTO responseUser = objectMapper.readValue(responseContent, UserDTO.class);

        assertThat(responseUser).isEqualTo(userDTO);
    }

    @Test
    @DisplayName("Тест ошибки при регистрации пользователя")
    void testRegistrationUserConflict() throws Exception {
        String errorMessage = "User already exists";
        when(administrationService.registrationUser(any(RegistrationDTO.class))).thenThrow(new UserAlreadyExistsException(errorMessage));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_USER_REGISTRATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isConflict())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).contains(errorMessage);
    }
}

