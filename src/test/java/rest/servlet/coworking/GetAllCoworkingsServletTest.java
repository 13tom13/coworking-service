package rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.coworking.GetAllCoworkingsServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.CoworkingServletTest;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование сервлета для получения всех коворкингов")
public class GetAllCoworkingsServletTest extends CoworkingServletTest {

    @InjectMocks
    private GetAllCoworkingsServlet servlet;

    @BeforeEach
    public void setService() throws NoSuchFieldException, IllegalAccessException {
        injectMocksCoworkingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное получение всех коворкингов")
    public void testDoGetSuccess() throws Exception {
        when(request.getMethod()).thenReturn(GET_METHOD);

        Map<String, CoworkingDTO> allCoworking = Map.of(coworkingName, coworkingDTO);
        when(coworkingService.getAllCoworking()).thenReturn(allCoworking);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Ошибка при получении всех коворкингов")
    public void testDoGetRepositoryException() throws Exception {
        when(request.getMethod()).thenReturn(GET_METHOD);

        when(coworkingService.getAllCoworking()).thenThrow(new RepositoryException(repositoryException));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка репозитория: " + repositoryException);
    }
}