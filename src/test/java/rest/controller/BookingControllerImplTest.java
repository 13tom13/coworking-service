package rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.BookingControllerImpl;
import io.ylab.tom13.coworkingservice.in.rest.services.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerImplTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingControllerImpl bookingController;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {

        Field bookingRepositoryField = BookingControllerImpl.class.getDeclaredField("bookingService");
        bookingRepositoryField.setAccessible(true);
        bookingRepositoryField.set(bookingController, bookingService);
    }

    @Test
    void createBooking_success() throws BookingConflictException, RepositoryException {
        BookingDTO bookingDTO = new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING));
        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        ResponseDTO<BookingDTO> response = bookingController.createBooking(bookingDTO);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookingDTO);
        assertThat(response.message()).isNull();
    }

    @Test
    void createBooking_conflict() throws BookingConflictException, RepositoryException {
        BookingDTO bookingDTO = new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING));
        when(bookingService.createBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException("Conflict"));

        ResponseDTO<BookingDTO> response = bookingController.createBooking(bookingDTO);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Conflict");
    }

    @Test
    void cancelBooking_success() throws BookingNotFoundException {
        doNothing().when(bookingService).cancelBooking(1L);

        ResponseDTO<Void> response = bookingController.cancelBooking(1L);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isNull();
    }

    @Test
    void cancelBooking_notFound() throws BookingNotFoundException {
        doThrow(new BookingNotFoundException("Not Found")).when(bookingService).cancelBooking(1L);

        ResponseDTO<Void> response = bookingController.cancelBooking(1L);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    void getBookingsByUser_success() throws BookingNotFoundException {
        List<BookingDTO> bookings = List.of(new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING)));
        when(bookingService.getBookingsByUser(1L)).thenReturn(bookings);

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUser(1L);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookings);
        assertThat(response.message()).isNull();
    }

    @Test
    void getBookingsByUser_notFound() throws BookingNotFoundException {
        when(bookingService.getBookingsByUser(1L)).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUser(1L);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    void getBookingsByUserAndDate_success() throws BookingNotFoundException {
        LocalDate date = LocalDate.now();
        List<BookingDTO> bookings = List.of(new BookingDTO(1L, 1L, 1L, date, List.of(TimeSlot.MORNING)));
        when(bookingService.getBookingsByUserAndDate(1L, date)).thenReturn(bookings);

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndDate(1L, date);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookings);
        assertThat(response.message()).isNull();
    }

    @Test
    void getBookingsByUserAndDate_notFound() throws BookingNotFoundException {
        LocalDate date = LocalDate.now();
        when(bookingService.getBookingsByUserAndDate(1L, date)).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndDate(1L, date);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    void getBookingsByUserAndCoworking_success() throws BookingNotFoundException {
        List<BookingDTO> bookings = List.of(new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING)));
        when(bookingService.getBookingsByUserAndCoworking(1L, 1L)).thenReturn(bookings);

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndCoworking(1L, 1L);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(bookings);
        assertThat(response.message()).isNull();
    }

    @Test
    void getBookingsByUserAndCoworking_notFound() throws BookingNotFoundException {
        when(bookingService.getBookingsByUserAndCoworking(1L, 1L)).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<List<BookingDTO>> response = bookingController.getBookingsByUserAndCoworking(1L, 1L);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    void getAvailableSlots_success() {
        LocalDate date = LocalDate.now();
        List<TimeSlot> slots = List.of(TimeSlot.MORNING, TimeSlot.AFTERNOON);
        when(bookingService.getAvailableSlots(1L, date)).thenReturn(slots);

        ResponseDTO<List<TimeSlot>> response = bookingController.getAvailableSlots(1L, date);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(slots);
        assertThat(response.message()).isNull();
    }

    @Test
    void getBookingById_success() throws BookingNotFoundException {
        BookingDTO booking = new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING));
        when(bookingService.getBookingById(1L)).thenReturn(booking);

        ResponseDTO<BookingDTO> response = bookingController.getBookingById(1L);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(booking);
        assertThat(response.message()).isNull();
    }

    @Test
    void getBookingById_notFound() throws BookingNotFoundException {
        when(bookingService.getBookingById(1L)).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<BookingDTO> response = bookingController.getBookingById(1L);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    void updateBooking_success() throws BookingNotFoundException, BookingConflictException, RepositoryException {
        BookingDTO booking = new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING));
        when(bookingService.updateBooking(any(BookingDTO.class))).thenReturn(booking);

        ResponseDTO<BookingDTO> response = bookingController.updateBooking(booking);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo(booking);
        assertThat(response.message()).isNull();
    }

    @Test
    void updateBooking_notFound() throws BookingNotFoundException, BookingConflictException, RepositoryException {
        BookingDTO booking = new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING));
        when(bookingService.updateBooking(any(BookingDTO.class))).thenThrow(new BookingNotFoundException("Not Found"));

        ResponseDTO<BookingDTO> response = bookingController.updateBooking(booking);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Not Found");
    }

    @Test
    void updateBooking_conflict() throws BookingNotFoundException, BookingConflictException, RepositoryException {
        BookingDTO booking = new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING));
        when(bookingService.updateBooking(any(BookingDTO.class))).thenThrow(new BookingConflictException("Conflict"));

        ResponseDTO<BookingDTO> response = bookingController.updateBooking(booking);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Conflict");
    }

}