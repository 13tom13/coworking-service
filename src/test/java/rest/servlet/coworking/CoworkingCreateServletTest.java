package rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.coworking.CoworkingCreateServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.CoworkingServletTest;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;

@DisplayName("Тестирование сервлета для создания коворкинга")
public class CoworkingCreateServletTest extends CoworkingServletTest {

    @InjectMocks
    private CoworkingCreateServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksCoworkingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное создание коворкинга")
    public void testDoPostSuccess() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(workplaceDTOJson)));

        when(coworkingService.createCoworking(any(CoworkingDTO.class))).thenReturn(coworkingDTO);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Конфликт при создании коворкинга")
    public void testDoPostConflict() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(workplaceDTOJson)));

        when(coworkingService.createCoworking(any(CoworkingDTO.class))).thenThrow(new CoworkingConflictException("Conflict"));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_CONFLICT, "Conflict");
    }
}