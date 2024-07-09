package rest.servlet.booking;

import io.ylab.tom13.coworkingservice.out.rest.servlet.booking.GetAvailableSlotsForBookingServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.BookingServletTest;

import java.time.LocalDate;

import static io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServiceServlet.COWORKING_ID;
import static io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServiceServlet.DATE;
import static org.mockito.Mockito.*;

@DisplayName("Тестирование сервлета для получения доступных слотов для бронирования")
public class GetAvailableSlotsForBookingServletTest extends BookingServletTest {

    @InjectMocks
    private GetAvailableSlotsForBookingServlet servlet;

    @BeforeEach
    public void setService() throws NoSuchFieldException, IllegalAccessException {
        injectMocksBookingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное получение доступных слотов")
    public void testDoGetSuccess() throws Exception {
        when(request.getMethod()).thenReturn(GET_METHOD);
        when(request.getParameter(COWORKING_ID)).thenReturn(String.valueOf(coworkingId));
        when(request.getParameter(DATE)).thenReturn(String.valueOf(date));

        when(bookingService.getAvailableSlots(eq(coworkingId), any(LocalDate.class))).thenReturn(timeSlots);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Неверный формат параметра")
    public void testDoGetInvalidParam() throws Exception {
        when(request.getMethod()).thenReturn(GET_METHOD);
        when(request.getParameter(COWORKING_ID)).thenReturn("invalid");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат параметра");
    }
}
