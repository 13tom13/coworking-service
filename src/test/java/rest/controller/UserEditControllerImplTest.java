package rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.controller.user.implementation.UserEditControllerImpl;
import io.ylab.tom13.coworkingservice.in.rest.services.user.UserEditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEditControllerImplTest {

    @Mock
    private UserEditService userEditService;

    @InjectMocks
    private UserEditControllerImpl userEditController;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field userEditControllerField = UserEditControllerImpl.class.getDeclaredField("userEditService");
        userEditControllerField.setAccessible(true);
        userEditControllerField.set(userEditController, userEditService);
    }

    @Test
    void testEditUserSuccess() throws RepositoryException {
        UserDTO userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com");
        when(userEditService.editUser(any(UserDTO.class))).thenReturn(userDTO);

        ResponseDTO<UserDTO> response = userEditController.editUser(userDTO);

        assertEquals(userDTO, response.data());
        verify(userEditService, times(1)).editUser(userDTO);
    }

    @Test
    void testEditUserRepositoryException() throws RepositoryException {
        UserDTO userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com");

        String errorMessage = "User not found";
        when(userEditService.editUser(userDTO)).thenThrow(new UserNotFoundException(errorMessage));

        // Вызов метода контроллера
        ResponseDTO<UserDTO> response = userEditController.editUser(userDTO);

        assertTrue(response.message().contains("User not found"));
        assertNull(response.data());
    }

    @Test
    void testEditPasswordSuccess() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("john.doe@example.com", "oldPassword", "newPassword");

        ResponseDTO<String> response = userEditController.editPassword(passwordChangeDTO);

        assertEquals("Пароль успешно изменен", response.data());
    }

}

