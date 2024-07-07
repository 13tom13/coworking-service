package rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.rest.controller.implementation.BookingControllerImpl;
import io.ylab.tom13.coworkingservice.out.rest.services.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.SecurityHTTPControllerTest;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Тесты сервиса бронирований")
class BookingControllerImplTest extends SecurityHTTPControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingControllerImpl bookingController;

    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {

        Field bookingControllerField = BookingControllerImpl.class.getDeclaredField("bookingService");
        bookingControllerField.setAccessible(true);
        bookingControllerField.set(bookingController, bookingService);

        bookingDTO = new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING));
    }

    @Test
    @DisplayName("Тест создания бронирования")
    void createBooking_success() throws BookingConflictException, RepositoryException {
        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        ResponseDTO<BookingDTO> response = bookingController.createBooking(bookingDTO);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookingDTO);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ошибки при бронировании")
    void createBooking_conflict() throws BookingConflictException, RepositoryException {
        when(bookingService.createBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException("Conflict"));

        ResponseDTO<BookingDTO> response = bookingController.createBooking(bookingDTO);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Conflict");
    }

    @Test
    @DisplayName("Тест отмены бронирования")
    void cancelBooking_success() throws BookingNotFoundException, RepositoryException {
        doNothing().when(bookingService).cancelBooking(1L);

        ResponseDTO<Void> response = bookingController.cancelBooking(1L);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ошибки при отмене бронирования")
    void cancelBooking_notFound() throws BookingNotFoundException, RepositoryException {
        doThrow(new BookingNotFoundException("Not Found")).when(bookingService).cancelBooking(1L);

        ResponseDTO<Void> response = bookingController.cancelBooking(1L);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю")
    void getBookingsByUser_success() throws BookingNotFoundException, RepositoryException {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUser(bookingDTO.userId())).thenReturn(bookings);

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUser(bookingDTO.userId());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookings);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю")
    void getBookingsByUser_notFound() throws BookingNotFoundException, RepositoryException {
        when(bookingService.getBookingsByUser(bookingDTO.userId())).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUser(bookingDTO.userId());

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю и дате")
    void getBookingsByUserAndDate_success() throws BookingNotFoundException, RepositoryException {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date())).thenReturn(bookings);

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookings);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю и дате")
    void getBookingsByUserAndDate_notFound() throws BookingNotFoundException, RepositoryException {
        when(bookingService.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date())).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date());

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю и коворкингу")
    void getBookingsByUserAndCoworking_success() throws BookingNotFoundException, RepositoryException {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUserAndCoworking(bookingDTO.userId(), bookingDTO.coworkingId())).thenReturn(bookings);

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndCoworking(bookingDTO.userId(), bookingDTO.coworkingId());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookings);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю и коворкингу")
    void getBookingsByUserAndCoworking_notFound() throws BookingNotFoundException, RepositoryException {
        when(bookingService.getBookingsByUserAndCoworking(1L, 1L)).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndCoworking(1L, 1L);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест получения списка доступного времени бронирования")
    void getAvailableSlots_success() throws RepositoryException {
        List<TimeSlot> slots = List.of(TimeSlot.MORNING, TimeSlot.AFTERNOON);
        when(bookingService.getAvailableSlots(bookingDTO.coworkingId(), bookingDTO.date())).thenReturn(slots);

        ResponseDTO<List<TimeSlot>> response = bookingController.getAvailableSlots(bookingDTO.coworkingId(), bookingDTO.date());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(slots);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест получения бронирования по id")
    void getBookingById_success() throws BookingNotFoundException, RepositoryException {
        when(bookingService.getBookingById(bookingDTO.id())).thenReturn(bookingDTO);

        ResponseDTO<BookingDTO> response = bookingController.getBookingById(bookingDTO.id());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookingDTO);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ненахождение бронирования по id")
    void getBookingById_notFound() throws BookingNotFoundException, RepositoryException {
        when(bookingService.getBookingById(bookingDTO.id())).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<BookingDTO> response = bookingController.getBookingById(bookingDTO.id());

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест обновление бронирования")
    void updateBooking_success() throws BookingNotFoundException, BookingConflictException, RepositoryException {
        when(bookingService.updateBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        ResponseDTO<BookingDTO> response = bookingController.updateBooking(bookingDTO);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookingDTO);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ненахождения бронирования при обновлении")
    void updateBooking_notFound() throws BookingNotFoundException, BookingConflictException, RepositoryException {
        when(bookingService.updateBooking(any(BookingDTO.class))).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<BookingDTO> response = bookingController.updateBooking(bookingDTO);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест конфликта при обновлении бронирования")
    void updateBooking_conflict() throws BookingNotFoundException, BookingConflictException, RepositoryException {
        when(bookingService.updateBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException("Conflict"));

        ResponseDTO<BookingDTO> response = bookingController.updateBooking(bookingDTO);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Conflict");
    }

}