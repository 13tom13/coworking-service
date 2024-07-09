package rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.coworking.CoworkingDeleteServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.CoworkingServletTest;

import static io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServiceServlet.COWORKING_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Тестирование сервлета для удаления коворкинга")
public class CoworkingDeleteServletTest extends CoworkingServletTest {

    @InjectMocks
    private CoworkingDeleteServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksCoworkingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное удаление коворкинга")
    public void testDoDeleteSuccess() throws Exception {
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getParameter(COWORKING_ID)).thenReturn(String.valueOf(coworkingId));

        doNothing().when(coworkingService).deleteCoworking(coworkingId);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String actualResponse = responseWriter.toString();
        assertEquals("\"Коворкинг успешно удален\"", actualResponse);
    }

    @Test
    @DisplayName("Коворкинг не найден")
    public void testDoDeleteCoworkingNotFound() throws Exception {
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getParameter(COWORKING_ID)).thenReturn(String.valueOf(coworkingId));

        doThrow(new CoworkingNotFoundException("Not found")).when(coworkingService).deleteCoworking(coworkingId);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
    }
}
