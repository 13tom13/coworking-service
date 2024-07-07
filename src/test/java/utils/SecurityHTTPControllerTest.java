package utils;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.utils.mapper.UserMapper;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("Создание заглушки для безопасности для тестирования контроллеров")
public class SecurityHTTPControllerTest {

    @Mock
    protected Session session;

    @Mock
    protected UserRepository userRepository;

    @BeforeEach
    public void getAccess() {
        UserDTO userDTO = new UserDTO(3L, "John", "Doe", "john.doe@example.com", Role.ADMINISTRATOR);
        Optional<User> user = Optional.ofNullable(UserMapper.INSTANCE.toUser(userDTO));

        lenient().when(session.getUser()).thenReturn(Optional.of(userDTO));
        lenient().doReturn(user).when(userRepository).findById(anyLong());
    }

}
