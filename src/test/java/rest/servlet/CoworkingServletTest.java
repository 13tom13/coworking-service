package rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServlet;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import utils.ServletTest;

import java.lang.reflect.Field;

@DisplayName("Создание заглушки для сервиса работы с коворкингами")
public abstract class CoworkingServletTest extends ServletTest {


    @Mock
    protected CoworkingService coworkingService;

    protected void injectMocksCoworkingServiceIntoServlet(Object servlet) throws NoSuchFieldException, IllegalAccessException {
        Field coworkingServiceField = CoworkingServlet.class.getDeclaredField("coworkingService");
        coworkingServiceField.setAccessible(true);
        coworkingServiceField.set(servlet, coworkingService);
    }
}
