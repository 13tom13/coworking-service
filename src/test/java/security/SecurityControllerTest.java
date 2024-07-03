package security;

import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.utils.security.SecurityController;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
@DisplayName("Создание заглушки для безопасности для тестирования контроллеров")
public class SecurityControllerTest {

    @Mock
    protected Session session;

    @Mock
    protected UserRepository userRepository;

    @Mock
    protected SecurityController securityController;


    @BeforeEach
    void setUpBase() throws NoSuchFieldException, IllegalAccessException {
        Field sessionField = SecurityController.class.getDeclaredField("session");
        sessionField.setAccessible(true);
        sessionField.set(securityController, session);

        Field coworkingServiceField = SecurityController.class.getDeclaredField("userRepository");
        coworkingServiceField.setAccessible(true);
        coworkingServiceField.set(securityController, userRepository);
    }


}
