package rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.rest.servlet.coworking.GetAllAvailableCoworkingsServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.CoworkingServletTest;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование сервлета для получения всех доступных коворкингов")
public class GetAllAvailableCoworkingsServletTest extends CoworkingServletTest {

    @InjectMocks
    private GetAllAvailableCoworkingsServlet servlet;

    @BeforeEach
    public void setService() throws NoSuchFieldException, IllegalAccessException {
        injectMocksCoworkingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное получение всех доступных коворкингов")
    public void testDoGetSuccess() throws Exception {
        when(request.getMethod()).thenReturn(GET_METHOD);

        Map<String, CoworkingDTO> availableCoworkings = Map.of(coworkingName, coworkingDTO);
        when(coworkingService.getAllAvailableCoworkings()).thenReturn(availableCoworkings);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
