package rest.servlet.booking;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.booking.BookingUpdateServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.BookingServletTest;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;

@DisplayName("Тестирование сервлета для управления бронирования")
public class BookingUpdateServletTest extends BookingServletTest {

    @InjectMocks
    private BookingUpdateServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksBookingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное обновление бронирования")
    public void testDoPostSuccess() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bookingDTOJson)));

        when(bookingService.updateBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Конфликт при обновлении бронирования")
    public void testDoPostConflict() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bookingDTOJson)));

        when(bookingService.updateBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException("Conflict"));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_CONFLICT, "Conflict");
    }

    @Test
    @DisplayName("Бронирование не найдено")
    public void testDoPostNotFound() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bookingDTOJson)));

        when(bookingService.updateBooking(any(BookingDTO.class))).thenThrow(new BookingNotFoundException("Not found"));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
    }
}
