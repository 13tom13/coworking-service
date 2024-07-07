package rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.controller.implementation.UserEditControllerImpl;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import security.SecurityHTTPControllerTest;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты контроллера редактирования пользователя")
public class UserEditControllerImplTest extends SecurityHTTPControllerTest {

    @Mock
    private UserEditService userEditService;

    @InjectMocks
    private UserEditControllerImpl userEditController;

    private UserDTO userDTO;


    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field userEditControllerField = UserEditControllerImpl.class.getDeclaredField("userEditService");
        userEditControllerField.setAccessible(true);
        userEditControllerField.set(userEditController, userEditService);

        userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com", Role.USER);

        when(session.getUser()).thenReturn(Optional.of(userDTO));
    }

    @Test
    @DisplayName("Тест успешно редактировать пользователя")
    void testEditUserSuccess() throws RepositoryException, UserNotFoundException, UserAlreadyExistsException {
        when(userEditService.editUser(userDTO)).thenReturn(userDTO);

        ResponseDTO<UserDTO> response = userEditController.editUser(userDTO);

        assertEquals(userDTO, response.data());
        verify(userEditService, times(1)).editUser(userDTO);
    }

    @Test
    @DisplayName("Тест ошибки при редактировании пользователя")
    void testEditUserRepositoryException() throws RepositoryException, UserNotFoundException, UserAlreadyExistsException {

        String errorMessage = "User not found";
        when(userEditService.editUser(userDTO)).thenThrow(new UserNotFoundException(errorMessage));

        ResponseDTO<UserDTO> response = userEditController.editUser(userDTO);

        assertTrue(response.message().contains("User not found"));
        assertNull(response.data());
    }

    @Test
    @DisplayName("Тест успешно изменения пароля")
    void testEditPasswordSuccess() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("john.doe@example.com", "oldPassword", "newPassword");

        ResponseDTO<String> response = userEditController.editPassword(passwordChangeDTO);

        assertEquals("Пароль успешно изменен", response.data());
    }

}

