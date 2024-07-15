package rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.controller.implementation.BookingControllerSpring;
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

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingControllerSpring bookingController;

    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        bookingDTO = new BookingDTO(1L, 1L, 1L, LocalDate.now().plusDays(1L), List.of(TimeSlot.MORNING));
    }

    @Test
    @DisplayName("Тест создания бронирования")
    void createBooking_success() throws Exception {
        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        mockMvc.perform(post("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTO)));
    }

    @Test
    @DisplayName("Тест ошибки при бронировании")
    void createBooking_conflict() throws Exception {
        when(bookingService.createBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException("Conflict"));

        mockMvc.perform(post("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Conflict"));
    }

    @Test
    @DisplayName("Тест отмены бронирования")
    void cancelBooking_success() throws Exception {
        doNothing().when(bookingService).cancelBooking(1L);

        mockMvc.perform(delete("/booking/cancel")
                        .param("bookingId", "1")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Бронирование отменено"));
    }

    @Test
    @DisplayName("Тест ошибки при отмене бронирования")
    void cancelBooking_notFound() throws Exception {
        doThrow(new BookingNotFoundException("Not Found")).when(bookingService).cancelBooking(1L);

        mockMvc.perform(delete("/booking/cancel")
                        .param("bookingId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found"));
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю")
    void getBookingsByUser_success() throws Exception {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUser(bookingDTO.userId())).thenReturn(bookings);

        mockMvc.perform(get("/booking/user")
                        .param("userId", String.valueOf(bookingDTO.userId())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookings)));
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю")
    void getBookingsByUser_notFound() throws Exception {
        when(bookingService.getBookingsByUser(bookingDTO.userId())).thenThrow(new BookingNotFoundException("Not Found"));

        mockMvc.perform(get("/booking/user")
                        .param("userId", String.valueOf(bookingDTO.userId())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found"));
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю и дате")
    void getBookingsByUserAndDate_success() throws Exception {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date())).thenReturn(bookings);

        mockMvc.perform(get("/booking/user/date")
                        .param("userId", String.valueOf(bookingDTO.userId()))
                        .param("date", bookingDTO.date().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookings)));
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю и дате")
    void getBookingsByUserAndDate_notFound() throws Exception {
        when(bookingService.getBookingsByUserAndDate(bookingDTO.userId(), bookingDTO.date())).thenThrow(new BookingNotFoundException("Not Found"));

        mockMvc.perform(get("/booking/user/date")
                        .param("userId", String.valueOf(bookingDTO.userId()))
                        .param("date", bookingDTO.date().toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found"));
    }

    @Test
    @DisplayName("Тест получения списка бронирований по пользователю и коворкингу")
    void getBookingsByUserAndCoworking_success() throws Exception {
        List<BookingDTO> bookings = List.of(bookingDTO);
        when(bookingService.getBookingsByUserAndCoworking(bookingDTO.userId(), bookingDTO.coworkingId())).thenReturn(bookings);

        mockMvc.perform(get("/booking/user/coworking")
                        .param("userId", String.valueOf(bookingDTO.userId()))
                        .param("coworkingId", String.valueOf(bookingDTO.coworkingId())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookings)));
    }

    @Test
    @DisplayName("Тест ненахождение бронирований по пользователю и коворкингу")
    void getBookingsByUserAndCoworking_notFound() throws Exception {
        when(bookingService.getBookingsByUserAndCoworking(bookingDTO.userId(), bookingDTO.coworkingId())).thenThrow(new BookingNotFoundException("Not Found"));

        mockMvc.perform(get("/booking/user/coworking")
                        .param("userId", String.valueOf(bookingDTO.userId()))
                        .param("coworkingId", String.valueOf(bookingDTO.coworkingId())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found"));
    }

    @Test
    @DisplayName("Тест получения списка доступного времени бронирования")
    void getAvailableSlots_success() throws Exception {
        List<TimeSlot> slots = List.of(TimeSlot.MORNING, TimeSlot.AFTERNOON);
        when(bookingService.getAvailableSlots(bookingDTO.coworkingId(), bookingDTO.date())).thenReturn(slots);

        mockMvc.perform(get("/booking/availableslots")
                        .param("coworkingId", String.valueOf(bookingDTO.coworkingId()))
                        .param("date", bookingDTO.date().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(slots)));
    }

    @Test
    @DisplayName("Тест получения бронирования по id")
    void getBookingById_success() throws Exception {
        when(bookingService.getBookingById(bookingDTO.id())).thenReturn(bookingDTO);

        mockMvc.perform(get("/booking")
                        .param("bookingId", String.valueOf(bookingDTO.id())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTO)));
    }

    @Test
    @DisplayName("Тест ненахождение бронирования по id")
    void getBookingById_notFound() throws Exception {
        when(bookingService.getBookingById(bookingDTO.id())).thenThrow(new BookingNotFoundException("Not Found"));

        mockMvc.perform(get("/booking")
                        .param("bookingId", String.valueOf(bookingDTO.id())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found"));
    }

    @Test
    @DisplayName("Тест обновление бронирования")
    void updateBooking_success() throws Exception {
        when(bookingService.updateBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        mockMvc.perform(patch("/booking/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDTO)));
    }

    @Test
    @DisplayName("Тест ошибки при обновлении бронирования")
    void updateBooking_conflict() throws Exception {
        when(bookingService.updateBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException("Conflict"));

        mockMvc.perform(patch("/booking/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Conflict"));
    }
}
