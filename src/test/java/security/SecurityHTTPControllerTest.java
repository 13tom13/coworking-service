package security;

import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.utils.security.SecurityHTTPController;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
@DisplayName("Создание заглушки для безопасности для тестирования контроллеров")
public class SecurityHTTPControllerTest {

    @Mock
    protected Session session;

    @Mock
    protected UserRepository userRepository;

    @Mock
    protected SecurityHTTPController securityController;


    @BeforeEach
    void setUpBase() throws NoSuchFieldException, IllegalAccessException {
        Field sessionField = SecurityHTTPController.class.getDeclaredField("session");
        sessionField.setAccessible(true);
        sessionField.set(securityController, session);

        Field coworkingServiceField = SecurityHTTPController.class.getDeclaredField("userRepository");
        coworkingServiceField.setAccessible(true);
        coworkingServiceField.set(securityController, userRepository);
    }


}
