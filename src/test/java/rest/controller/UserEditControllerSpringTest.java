package rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.UserEditControllerSpring;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты контроллера редактирования пользователя")
public class UserEditControllerSpringTest extends MvcTest {

    private static final String USER_EDIT_URL = "/user/edit";
    private static final String USER_EDIT_PASSWORD_URL = "/user/edit/password";

    @Mock
    private UserEditService userEditService;

    @InjectMocks
    private UserEditControllerSpring userEditController;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userEditController).build();
        userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com", Role.USER);
    }

    @Test
    @DisplayName("Тест успешно редактировать пользователя")
    void testEditUserSuccess() throws Exception {
        when(userEditService.editUser(any(UserDTO.class))).thenReturn(userDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(USER_EDIT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        UserDTO responseUser = objectMapper.readValue(responseContent, UserDTO.class);

        assertThat(responseUser).isEqualTo(userDTO);
    }

    @Test
    @DisplayName("Тест ошибки при редактировании пользователя")
    void testEditUserRepositoryException() throws Exception {
        String errorMessage = "User not found";
        when(userEditService.editUser(any(UserDTO.class))).thenThrow(new UserNotFoundException(errorMessage));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(USER_EDIT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).contains(errorMessage);
    }

    @Test
    @DisplayName("Тест успешно изменения пароля")
    void testEditPasswordSuccess() throws Exception {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("john.doe@example.com", "oldPassword", "newPassword");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(USER_EDIT_PASSWORD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordChangeDTO));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).isEqualTo("Пароль успешно изменен");
    }

    @Test
    @DisplayName("Тест ошибки при изменении пароля")
    void testEditPasswordUnauthorizedException() throws Exception {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("john.doe@example.com", "oldPassword", "newPassword");
        String errorMessage = "Unauthorized access";

        doThrow(new UnauthorizedException(errorMessage)).when(userEditService).editPassword(any(PasswordChangeDTO.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(USER_EDIT_PASSWORD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordChangeDTO));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).contains(errorMessage);
    }
}

