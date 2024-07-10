package rest.servlet.booking;


import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.booking.BookingCancelServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.BookingServletTest;

import static io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServiceServlet.BOOKING_ID;
import static io.ylab.tom13.coworkingservice.out.rest.servlet.booking.BookingCancelServlet.RESPONSE_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Тестирование сервлета для отмены бронирования")
public class BookingCancelServletTest extends BookingServletTest {

    @InjectMocks
    private BookingCancelServlet servlet;

    @BeforeEach
    public void setService() throws NoSuchFieldException, IllegalAccessException {
        injectMocksBookingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное выполнение отмены бронирования")
    public void testDoDeleteSuccess() throws Exception {
        String expectedResponse = "\"" + RESPONSE_SUCCESS + "\"";
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getParameter(BOOKING_ID)).thenReturn(String.valueOf(userId));

        doNothing().when(bookingService).cancelBooking(userId);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String actualResponse = responseWriter.toString();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Бронирование не найдено")
    public void testDoDeleteBookingNotFound() throws Exception {
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getParameter(BOOKING_ID)).thenReturn(String.valueOf(userId));

        doThrow(new BookingNotFoundException(String.valueOf(userId))).when(bookingService).cancelBooking(userId);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, String.valueOf(userId));
    }
}
