package rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.BookingControllerImpl;
import io.ylab.tom13.coworkingservice.in.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import security.SecurityControllerTest;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса бронирований")
class BookingControllerImplTest extends SecurityControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingControllerImpl bookingController;

    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {

        Field bookingRepositoryField = BookingControllerImpl.class.getDeclaredField("bookingService");
        bookingRepositoryField.setAccessible(true);
        bookingRepositoryField.set(bookingController, bookingService);

        UserDTO userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com", Role.ADMINISTRATOR);
        Optional<User> user = Optional.ofNullable(UserMapper.INSTANCE.toUser(userDTO));

        when(session.getUser()).thenReturn(userDTO);
        lenient().doReturn(user).when(userRepository).findById(anyLong());

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
    void cancelBooking_success() throws BookingNotFoundException {
        doNothing().when(bookingService).cancelBooking(1L);

        ResponseDTO<Void> response = bookingController.cancelBooking(1L);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ошибки при отмене бронирования")
    void cancelBooking_notFound() throws BookingNotFoundException {
        doThrow(new BookingNotFoundException("Not Found")).when(bookingService).cancelBooking(1L);

        ResponseDTO<Void> response = bookingController.cancelBooking(1L);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю")
    void getBookingsByUser_success() throws BookingNotFoundException {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUser(bookingDTO.userId())).thenReturn(bookings);

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUser(bookingDTO.userId());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookings);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю")
    void getBookingsByUser_notFound() throws BookingNotFoundException {
        when(bookingService.getBookingsByUser(bookingDTO.userId())).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUser(bookingDTO.userId());

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю и дате")
    void getBookingsByUserAndDate_success() throws BookingNotFoundException {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date())).thenReturn(bookings);

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookings);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю и дате")
    void getBookingsByUserAndDate_notFound() throws BookingNotFoundException {
        when(bookingService.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date())).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date());

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю и коворкингу")
    void getBookingsByUserAndCoworking_success() throws BookingNotFoundException {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUserAndCoworking(bookingDTO.userId(), bookingDTO.coworkingId())).thenReturn(bookings);

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndCoworking(bookingDTO.userId(), bookingDTO.coworkingId());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookings);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю и коворкингу")
    void getBookingsByUserAndCoworking_notFound() throws BookingNotFoundException {
        when(bookingService.getBookingsByUserAndCoworking(1L, 1L)).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndCoworking(1L, 1L);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Тест получения списка доступного времени бронирования")
    void getAvailableSlots_success() {
        List<TimeSlot> slots = List.of(TimeSlot.MORNING, TimeSlot.AFTERNOON);
        when(bookingService.getAvailableSlots(bookingDTO.coworkingId(), bookingDTO.date())).thenReturn(slots);

        ResponseDTO<List<TimeSlot>> response = bookingController.getAvailableSlots(bookingDTO.coworkingId(), bookingDTO.date());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(slots);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест получения бронирования по id")
    void getBookingById_success() throws BookingNotFoundException {
        when(bookingService.getBookingById(bookingDTO.id())).thenReturn(bookingDTO);

        ResponseDTO<BookingDTO> response = bookingController.getBookingById(bookingDTO.id());

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookingDTO);
        assertThat(response.message()).isNull();
    }

    @Test
    @DisplayName("Тест ненахождение бронирования по id")
    void getBookingById_notFound() throws BookingNotFoundException {
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