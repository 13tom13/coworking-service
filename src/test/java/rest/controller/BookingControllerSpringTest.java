package rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.out.exceptions.GlobalExceptionHandler;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.controller.BookingControllerSpring;
import io.ylab.tom13.coworkingservice.out.rest.services.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.MvcTest;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тесты сервиса бронирований")
class BookingControllerSpringTest extends MvcTest {

    private static final String BOOKING_CREATE = "/booking/create";
    private static final String BOOKING_CANCEL = "/booking/cancel";
    private static final String BOOKING_USER = "/booking/user";
    private static final String BOOKING_USER_DATE = "/booking/user/date";
    private static final String BOOKING_USER_COWORKING = "/booking/user/coworking";
    private static final String BOOKING_AVAILABLESLOTS = "/booking/availableslots";
    private static final String BOOKING = "/booking";
    private static final String BOOKING_UPDATE = "/booking/update";

    private static final String USER_ID = "userId";
    private static final String BOOKING_ID = "bookingId";
    private static final String COWORKING_ID = "coworkingId";
    private static final String DATE = "date";

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingControllerSpring bookingController;

    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        bookingDTO = new BookingDTO(1L, 1L, 1L, LocalDate.now().plusDays(1L), List.of(TimeSlot.MORNING));
    }

    @Test
    @DisplayName("Тест создания бронирования")
    void createBooking_success() throws Exception {
        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        mockMvc.perform(post(BOOKING_CREATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTO)));
    }

    @Test
    @DisplayName("Тест ошибки при бронировании")
    void createBooking_conflict() throws Exception {
        String conflict = "Conflict";
        when(bookingService.createBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException(conflict));

        mockMvc.perform(post(BOOKING_CREATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string(conflict));
    }

    @Test
    @DisplayName("Тест отмены бронирования")
    void cancelBooking_success() throws Exception {
        doNothing().when(bookingService).cancelBooking(1L);

        mockMvc.perform(delete(BOOKING_CANCEL)
                        .param(BOOKING_ID, String.valueOf(bookingDTO.id()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Тест ошибки при отмене бронирования")
    void cancelBooking_notFound() throws Exception {
        String message = "Not Found";
        doThrow(new BookingNotFoundException(message)).when(bookingService).cancelBooking(bookingDTO.id());

        mockMvc.perform(delete(BOOKING_CANCEL)
                        .param(BOOKING_ID, String.valueOf(bookingDTO.id())))
                .andExpect(status().isNotFound())
                .andExpect(content().string(message));
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю")
    void getBookingsByUser_success() throws Exception {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUser(bookingDTO.userId())).thenReturn(bookings);

        mockMvc.perform(get(BOOKING_USER)
                        .param(USER_ID, String.valueOf(bookingDTO.userId())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookings)));
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю")
    void getBookingsByUser_notFound() throws Exception {
        String message = "Not Found";
        when(bookingService.getBookingsByUser(bookingDTO.userId())).thenThrow(new BookingNotFoundException(message));

        mockMvc.perform(get(BOOKING_USER)
                        .param(USER_ID, String.valueOf(bookingDTO.userId())))
                .andExpect(status().isNotFound())
                .andExpect(content().string(message));
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю и дате")
    void getBookingsByUserAndDate_success() throws Exception {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date())).thenReturn(bookings);

        mockMvc.perform(get(BOOKING_USER_DATE)
                        .param(USER_ID, String.valueOf(bookingDTO.userId()))
                        .param(DATE, bookingDTO.date().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookings)));
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю и дате")
    void getBookingsByUserAndDate_notFound() throws Exception {
        String message = "Not Found";
        when(bookingService.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date())).thenThrow(new BookingNotFoundException(message));

        mockMvc.perform(get(BOOKING_USER_DATE)
                        .param(USER_ID, String.valueOf(bookingDTO.userId()))
                        .param(DATE, bookingDTO.date().toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(message));
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю и коворкингу")
    void getBookingsByUserAndCoworking_success() throws Exception {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUserAndCoworking(bookingDTO.userId(),
                bookingDTO.coworkingId())).thenReturn(bookings);

        mockMvc.perform(get(BOOKING_USER_COWORKING)
                        .param(USER_ID, String.valueOf(bookingDTO.userId()))
                        .param(COWORKING_ID, String.valueOf(bookingDTO.coworkingId())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookings)));
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю и коворкингу")
    void getBookingsByUserAndCoworking_notFound() throws Exception {
        String message = "Not Found";
        when(bookingService.getBookingsByUserAndCoworking(bookingDTO.userId(),
                bookingDTO.coworkingId())).thenThrow(new BookingNotFoundException(message));

        mockMvc.perform(get(BOOKING_USER_COWORKING)
                        .param(USER_ID, String.valueOf(bookingDTO.userId()))
                        .param(COWORKING_ID, String.valueOf(bookingDTO.coworkingId())))
                .andExpect(status().isNotFound())
                .andExpect(content().string(message));
    }

    @Test
    @DisplayName("Тест получения списка доступного времени бронирования")
    void getAvailableSlots_success() throws Exception {
        List<TimeSlot> slots = List.of(TimeSlot.MORNING, TimeSlot.AFTERNOON);
        when(bookingService.getAvailableSlots(bookingDTO.coworkingId(), bookingDTO.date())).thenReturn(slots);

        mockMvc.perform(get(BOOKING_AVAILABLESLOTS)
                        .param(COWORKING_ID, String.valueOf(bookingDTO.coworkingId()))
                        .param(DATE, bookingDTO.date().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(slots)));
    }

    @Test
    @DisplayName("Тест получения бронирования по id")
    void getBookingById_success() throws Exception {
        when(bookingService.getBookingById(bookingDTO.id())).thenReturn(bookingDTO);

        mockMvc.perform(get(BOOKING)
                        .param(BOOKING_ID, String.valueOf(bookingDTO.id())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTO)));
    }

    @Test
    @DisplayName("Тест ненахождение бронирования по id")
    void getBookingById_notFound() throws Exception {
        String message = "Not Found";
        when(bookingService.getBookingById(bookingDTO.id())).thenThrow(new BookingNotFoundException(message));

        mockMvc.perform(get(BOOKING)
                        .param(BOOKING_ID, String.valueOf(bookingDTO.id())))
                .andExpect(status().isNotFound())
                .andExpect(content().string(message));
    }

    @Test
    @DisplayName("Тест обновление бронирования")
    void updateBooking_success() throws Exception {
        when(bookingService.updateBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        mockMvc.perform(patch(BOOKING_UPDATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTO)));
    }

    @Test
    @DisplayName("Тест ошибки при обновлении бронирования")
    void updateBooking_conflict() throws Exception {
        String message = "Conflict";
        when(bookingService.updateBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException(message));

        mockMvc.perform(patch(BOOKING_UPDATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string(message));
    }
}
