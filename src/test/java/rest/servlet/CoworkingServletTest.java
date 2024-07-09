package rest.servlet;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import utils.ServletTest;

import java.io.IOException;
import java.lang.reflect.Field;

@DisplayName("Создание заглушки для сервиса работы с коворкингами")
public abstract class CoworkingServletTest extends ServletTest {


    @Mock
    protected CoworkingService coworkingService;

    protected CoworkingDTO coworkingDTO;

    protected String workplaceDTOJson;

    @BeforeEach
    public void setDTO() throws IOException {
        coworkingDTO = new WorkplaceDTO(coworkingId, coworkingName, coworkingDescription, available, workplaceType);
        workplaceDTOJson = objectMapper.writeValueAsString(coworkingDTO);
    }

    protected void injectMocksCoworkingServiceIntoServlet(Object servlet) throws NoSuchFieldException, IllegalAccessException {
        Field coworkingServiceField = CoworkingServlet.class.getDeclaredField("coworkingService");
        coworkingServiceField.setAccessible(true);
        coworkingServiceField.set(servlet, coworkingService);
    }
}
