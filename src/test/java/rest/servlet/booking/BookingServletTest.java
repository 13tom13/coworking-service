package rest.servlet.booking;

import io.ylab.tom13.coworkingservice.out.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.out.rest.servlet.BookingServlet;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import utils.ServletTest;

import java.lang.reflect.Field;

@DisplayName("Создание заглушки для сервиса бронирования")
public abstract class BookingServletTest extends ServletTest {


    @Mock
    protected BookingService bookingService;

    protected void injectMocksBookingServiceIntoServlet(Object servlet) throws NoSuchFieldException, IllegalAccessException {
        Field bookingServiceField = BookingServlet.class.getDeclaredField("bookingService");
        bookingServiceField.setAccessible(true);
        bookingServiceField.set(servlet, bookingService);
    }
}
