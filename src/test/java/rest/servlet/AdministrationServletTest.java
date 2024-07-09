package rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.services.AdministrationService;
import io.ylab.tom13.coworkingservice.out.rest.servlet.AdministrationServlet;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import utils.ServletTest;

import java.lang.reflect.Field;

@DisplayName("Создание заглушки для сервиса администратора")
public abstract class AdministrationServletTest extends ServletTest {

    @Mock
    protected AdministrationService administrationService;

    protected void injectMocksAdminServiceIntoServlet(Object servlet) throws NoSuchFieldException, IllegalAccessException {
        Field administrationServiceField = AdministrationServlet.class.getDeclaredField("administrationService");
        administrationServiceField.setAccessible(true);
        administrationServiceField.set(servlet, administrationService);
    }
}
