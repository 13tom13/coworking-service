package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.UserEditServiceImpl;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса редактирования пользователя")
class UserEditServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEditServiceImpl userEditService;

    private User user;
    private UserDTO userDTO;
    private String password;

    @BeforeEach
    void setUp() throws Exception {
        password = "password";
        String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
        user = new User(1L, "John", "Doe", "john.new@example.com", hashedPassword, Role.USER);
        userDTO = UserMapper.INSTANCE.toUserDTO(user);
        Field UserEditServiceField = UserEditServiceImpl.class.getDeclaredField("userRepository");
        UserEditServiceField.setAccessible(true);
        UserEditServiceField.set(userEditService, userRepository);
    }

    @Test
    @DisplayName("Успешное редактирование пользователя")
    void testEditUserSuccess() throws RepositoryException {
        when(userRepository.updateUser(user)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByEmail(user.email())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(user.id())).thenReturn(Optional.ofNullable(user));

        assertDoesNotThrow(() -> userEditService.editUser(userDTO));
    }

    @Test
    @DisplayName("Попытка редактирования несуществующего пользователя")
    void testEditUserUserNotFound() {
        assertThatThrownBy(() -> userEditService.editUser(userDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("с ID 1");
    }

    @Test
    @DisplayName("Успешное изменение пароля")
    void testEditPasswordSuccess() throws UnauthorizedException, RepositoryException, UserNotFoundException {
        when(userRepository.findByEmail(user.email())).thenReturn(Optional.ofNullable(user));

        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(user.email(), password, "newPassword");

        userEditService.editPassword(passwordChangeDTO);

    }
}
