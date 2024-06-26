package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        Field bookingRepositoryField = BookingServiceImpl.class.getDeclaredField("bookingRepository");
        bookingRepositoryField.setAccessible(true);
        bookingRepositoryField.set(bookingService, bookingRepository);
    }

    @Test
    void testCreateBooking() throws BookingConflictException, RepositoryException {
        BookingDTO bookingDTO = new BookingDTO(1L, 1L, 1L, LocalDate.now(), Arrays.asList(TimeSlot.MORNING, TimeSlot.AFTERNOON));
        doReturn(bookingDTO).when(bookingRepository).createBooking(any(Booking.class));

        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);

        assertThat(createdBooking).isNotNull();
        assertThat(createdBooking.id()).isEqualTo(1L);
    }

    @Test
    void testCancelBooking() throws BookingNotFoundException {
        long bookingId = 1L;
        bookingService.cancelBooking(bookingId);

        Mockito.verify(bookingRepository, Mockito.times(1)).deleteBooking(bookingId);
    }

    @Test
    void testGetBookingsByUser() throws BookingNotFoundException {
        long userId = 1L;
        List<BookingDTO> bookings = new ArrayList<>();
        bookings.add(new BookingDTO(1L, userId, 1L, LocalDate.now(), List.of(TimeSlot.MORNING)));
        bookings.add(new BookingDTO(2L, userId, 2L, LocalDate.now(), List.of(TimeSlot.AFTERNOON)));

        doReturn(bookings).when(bookingRepository).getBookingsByUser(userId);

        List<BookingDTO> foundBookings = bookingService.getBookingsByUser(userId);

        assertThat(foundBookings).isNotNull();
        assertThat(foundBookings.size()).isEqualTo(2);
    }

    @Test
    void testGetAvailableSlots() {
        long coworkingId = 1L;
        LocalDate date = LocalDate.now();

        List<BookingDTO> bookings = new ArrayList<>();
        bookings.add(new BookingDTO(1L, 1L, coworkingId, date, List.of(TimeSlot.MORNING)));

        doReturn(bookings).when(bookingRepository).getBookingsByCoworkingAndDate(coworkingId, date);

        List<TimeSlot> availableSlots = bookingService.getAvailableSlots(coworkingId, date);

        assertThat(availableSlots).isNotNull();
        assertThat(availableSlots.size()).isEqualTo(2);
    }

    @Test
    void testGetBookingById() throws BookingNotFoundException {
        long bookingId = 1L;
        BookingDTO bookingDTO = new BookingDTO(bookingId, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING));
        doReturn(bookingDTO).when(bookingRepository).getBookingById(bookingId);

        BookingDTO foundBooking = bookingService.getBookingById(bookingId);

        assertThat(foundBooking).isNotNull();
        assertThat(foundBooking.id()).isEqualTo(bookingId);
    }

    @Test
    void testUpdateBooking() throws BookingNotFoundException, BookingConflictException, RepositoryException {
        BookingDTO bookingDTO = new BookingDTO(1L, 1L, 1L, LocalDate.now(), List.of(TimeSlot.MORNING));
        doReturn(bookingDTO).when(bookingRepository).updateBooking(any(Booking.class));

        BookingDTO updatedBooking = bookingService.updateBooking(bookingDTO);

        assertThat(updatedBooking).isNotNull();
        assertThat(updatedBooking.id()).isEqualTo(1L);
    }
}
