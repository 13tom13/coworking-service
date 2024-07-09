package rest.servlet.booking;


import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.booking.BookingsByIdServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServiceServlet.BOOKING_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование сервлета для получения бронированию по id")
public class BookingsByIdServletTest extends BookingServletTest {

    @InjectMocks
    private BookingsByIdServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksBookingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное получение бронирования по ID")
    public void testDoGetSuccess() throws Exception {
        when(request.getMethod()).thenReturn(GET_METHOD);
        when(request.getParameter(BOOKING_ID)).thenReturn(String.valueOf(bookingId));

        when(bookingService.getBookingById(bookingId)).thenReturn(bookingDTO);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Бронирование не найдено")
    public void testDoGetNotFound() throws Exception {
        when(request.getMethod()).thenReturn(GET_METHOD);
        when(request.getParameter(BOOKING_ID)).thenReturn(String.valueOf(bookingId));

        when(bookingService.getBookingById(bookingId)).thenThrow(new BookingNotFoundException("Not found"));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
    }
}
