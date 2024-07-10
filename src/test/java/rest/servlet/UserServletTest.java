package rest.servlet;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.out.rest.servlet.UserServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import utils.ServletTest;

import java.lang.reflect.Field;

@DisplayName("Создание заглушки для сервиса редактирования пользователя")
public abstract class UserServletTest extends ServletTest {

    @Mock
    protected UserEditService userEditService;

    protected String passwordChangeDTOJson;

    protected String newPassword = "newPassword";

    @BeforeEach
    public void setDTO() throws Exception {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(email, password, newPassword);
        passwordChangeDTOJson = objectMapper.writeValueAsString(passwordChangeDTO);
    }

    protected void injectMocksUserServiceIntoServlet(Object servlet) throws NoSuchFieldException, IllegalAccessException {
        Field userServiceField = UserServlet.class.getDeclaredField("userEditService");
        userServiceField.setAccessible(true);
        userServiceField.set(servlet, userEditService);
    }
}
