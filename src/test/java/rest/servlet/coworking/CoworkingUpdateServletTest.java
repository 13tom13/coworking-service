package rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.coworking.CoworkingUpdateServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.CoworkingServletTest;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование сервлета для обновления коворкинга")
public class CoworkingUpdateServletTest extends CoworkingServletTest {

    @InjectMocks
    private CoworkingUpdateServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksCoworkingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное обновление коворкинга")
    public void testDoPostSuccess() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(workplaceDTOJson)));

        when(coworkingService.updateCoworking(any(CoworkingDTO.class))).thenReturn(coworkingDTO);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Конфликт при обновлении коворкинга")
    public void testDoPostConflict() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(workplaceDTOJson)));

        when(coworkingService.updateCoworking(any(CoworkingDTO.class))).thenThrow(new CoworkingConflictException("Conflict"));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_CONFLICT, "Conflict");
    }

    @Test
    @DisplayName("Коворкинг не найден")
    public void testDoPostNotFound() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(workplaceDTOJson)));

        when(coworkingService.updateCoworking(any(CoworkingDTO.class))).thenThrow(new CoworkingNotFoundException("Not found"));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
    }
}
