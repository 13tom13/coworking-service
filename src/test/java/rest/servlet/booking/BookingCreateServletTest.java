package rest.servlet.booking;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.booking.BookingCreateServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.BookingServletTest;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;

@DisplayName("Тестирование сервлета для создания бронирования")
public class BookingCreateServletTest extends BookingServletTest {

    @InjectMocks
    private BookingCreateServlet servlet;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        injectMocksBookingServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное создание бронирования")
    public void testDoPostSuccess() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bookingDTOJson)));


        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Конфликт при создании бронирования")
    public void testDoPostConflict() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bookingDTOJson)));

        when(bookingService.createBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException("Conflict"));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_CONFLICT, "Conflict");
    }
}


