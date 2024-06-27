package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.BookingServiceImpl;
import io.ylab.tom13.coworkingservice.in.utils.mapper.BookingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса работы с бронированиями")
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    BookingDTO bookingDTO;
    Booking booking;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        Field bookingRepositoryField = BookingServiceImpl.class.getDeclaredField("bookingRepository");
        bookingRepositoryField.setAccessible(true);
        bookingRepositoryField.set(bookingService, bookingRepository);
        bookingDTO = new BookingDTO(1L, 1L, 1L, LocalDate.now(), Arrays.asList(TimeSlot.MORNING, TimeSlot.AFTERNOON));
        booking = BookingMapper.INSTANCE.toBooking(bookingDTO);
    }

    @Test
    @DisplayName("Тест успешного создания бронирования")
    void testCreateBooking() throws BookingConflictException, RepositoryException {
        when(bookingRepository.createBooking(any(Booking.class))).thenReturn(Optional.ofNullable(booking));

        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);

        assertThat(createdBooking).isNotNull();
        assertThat(bookingDTO.userId()).isEqualTo(createdBooking.userId());
    }

    @Test
    @DisplayName("Тест отмены бронирования")
    void testCancelBooking() throws BookingNotFoundException {
        bookingService.cancelBooking(bookingDTO.id());

        Mockito.verify(bookingRepository, Mockito.times(1)).deleteBooking(bookingDTO.id());
    }

    @Test
    @DisplayName("Тест ошибки при отмене бронирования")
    void testCancelBookingException() throws BookingNotFoundException {
        doThrow(BookingNotFoundException.class).when(bookingRepository).deleteBooking(bookingDTO.id());

        assertThrows(BookingNotFoundException.class, () -> bookingService.cancelBooking(bookingDTO.id()));
    }

    @Test
    @DisplayName("Тест получения бронирований пользователя")
    void testGetBookingsByUser() throws BookingNotFoundException {
        long userId = 1L;
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(1L, userId, 1L, LocalDate.now(), List.of(TimeSlot.MORNING)));
        bookings.add(new Booking(2L, userId, 2L, LocalDate.now(), List.of(TimeSlot.AFTERNOON)));

        doReturn(bookings).when(bookingRepository).getBookingsByUser(userId);

        List<BookingDTO> foundBookings = bookingService.getBookingsByUser(userId);

        assertThat(foundBookings).isNotNull();
        assertThat(foundBookings.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Тест получения свободных слотов для бронирования")
    void testGetAvailableSlots() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        doReturn(bookings).when(bookingRepository).getBookingsByCoworkingAndDate(booking.id(), booking.date());

        List<TimeSlot> availableSlots = bookingService.getAvailableSlots(booking.coworkingId(), booking.date());

        assertThat(availableSlots).isNotNull();
        assertThat(availableSlots.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Тест получения бронирования по id")
    void testGetBookingById() throws BookingNotFoundException {
        doReturn(Optional.ofNullable(booking)).when(bookingRepository).getBookingById(booking.id());

        BookingDTO foundBooking = bookingService.getBookingById(booking.id());

        assertThat(foundBooking).isNotNull();
        assertThat(foundBooking.id()).isEqualTo(booking.id());
    }

    @Test
    @DisplayName("Тест обновления бронирования")
    void testUpdateBooking() throws BookingNotFoundException, BookingConflictException, RepositoryException {

        doReturn(Optional.ofNullable(booking)).when(bookingRepository).updateBooking(any(Booking.class));

        BookingDTO updatedBooking = bookingService.updateBooking(bookingDTO);

        assertThat(updatedBooking).isNotNull();
        assertThat(updatedBooking.id()).isEqualTo(booking.id());
    }
}
