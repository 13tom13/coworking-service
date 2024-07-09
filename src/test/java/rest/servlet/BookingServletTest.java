package rest.servlet;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.out.rest.servlet.BookingServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import utils.ServletTest;

import java.io.IOException;
import java.lang.reflect.Field;

@DisplayName("Создание заглушки для сервиса бронирования")
public abstract class BookingServletTest extends ServletTest {


    @Mock
    protected BookingService bookingService;

    protected BookingDTO bookingDTO;

    protected String bookingDTOJson;

    @BeforeEach
    public void setDTO() throws IOException {
        bookingDTO = new BookingDTO(bookingId, userId, coworkingId, date, timeSlots);
        bookingDTOJson = objectMapper.writeValueAsString(bookingDTO);
    }

    protected void injectMocksBookingServiceIntoServlet(Object servlet) throws NoSuchFieldException, IllegalAccessException {
        Field bookingServiceField = BookingServlet.class.getDeclaredField("bookingService");
        bookingServiceField.setAccessible(true);
        bookingServiceField.set(servlet, bookingService);
    }
}
