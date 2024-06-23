package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.user.implementation.UserEditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEditServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEditServiceImpl userEditService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() throws Exception {
        userDTO = new UserDTO(1L, "John", "Doe", "john.new@example.com");


        Field UserEditServiceField = UserEditServiceImpl.class.getDeclaredField("userRepository");
        UserEditServiceField.setAccessible(true);
        UserEditServiceField.set(userEditService, userRepository);
    }

    @Test
    void testEditUserSuccess() throws RepositoryException {
        when(userRepository.updateUser(userDTO)).thenReturn(userDTO);

        UserDTO updatedUserDTO = userEditService.editUser(userDTO);

        ArgumentCaptor<UserDTO> captor = ArgumentCaptor.forClass(UserDTO.class);
        verify(userRepository, times(1)).updateUser(captor.capture());

        UserDTO capturedUserDTO = captor.getValue();
        assertThat(capturedUserDTO).isEqualTo(userDTO);

    }

    @Test
    void testEditUserUserNotFound() throws UserNotFoundException, UserAlreadyExistsException {
        doThrow(new UserNotFoundException("с ID 1")).when(userRepository).updateUser(any(UserDTO.class));

        assertThatThrownBy(() -> userEditService.editUser(userDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("с ID 1");
    }

    @Test
    void testEditPasswordSuccess() throws UnauthorizedException, RepositoryException {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("john.doe@example.com", "password", "newPassword");

        doNothing().when(userRepository).updatePassword(any(PasswordChangeDTO.class));
        userEditService.editPassword(passwordChangeDTO);
        verify(userRepository, times(1)).updatePassword(any(PasswordChangeDTO.class));
    }
}
