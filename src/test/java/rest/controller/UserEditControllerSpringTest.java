package rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.GlobalExceptionHandler;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.controller.UserEditControllerSpring;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.MvcTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты контроллера редактирования пользователя")
public class UserEditControllerSpringTest extends MvcTest {

    private static final String USER_EDIT_URL = "/user/edit";
    private static final String USER_EDIT_PASSWORD_URL = "/user/edit/password";

    @Mock
    private UserEditService userEditService;

    @InjectMocks
    private UserEditControllerSpring userEditController;

    private String userEmail;

    private UserDTO userDTO;
    private PasswordChangeDTO passwordChangeDTO;
    private String userDTOJsonString;
    private String passwordChangeJsonString;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(userEditController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        userEmail = "john.doe@example.com";

        userDTO = new UserDTO(1L, "John", "Doe", userEmail, Role.USER);
        passwordChangeDTO = new PasswordChangeDTO(userEmail, "oldPassword", "newPassword");
        userDTOJsonString = objectMapper.writeValueAsString(userDTO);
        passwordChangeJsonString = objectMapper.writeValueAsString(passwordChangeDTO);
    }

    @Test
    @DisplayName("Тест успешно редактировать пользователя")
    void testEditUserSuccess() throws Exception {
        when(userEditService.editUser(userDTO)).thenReturn(userDTO);

        mockMvc.perform(patch(USER_EDIT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJsonString))
                .andExpect(status().isOk())
                .andExpect(content().json(userDTOJsonString));
    }

    @Test
    @DisplayName("Тест ошибки при редактировании пользователя")
    void testEditUserRepositoryException() throws Exception {
        when(userEditService.editUser(userDTO)).thenThrow(new UserNotFoundException(userEmail));

        mockMvc.perform(patch(USER_EDIT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJsonString))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(userEmail)));
    }

    @Test
    @DisplayName("Тест успешно изменения пароля")
    void testEditPasswordSuccess() throws Exception {
        mockMvc.perform(patch(USER_EDIT_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passwordChangeJsonString))
                .andExpect(status().isOk());

    }
}

