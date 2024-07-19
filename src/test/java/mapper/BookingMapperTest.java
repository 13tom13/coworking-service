package mapper;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.out.entity.model.Booking;
import io.ylab.tom13.coworkingservice.out.utils.mapper.BookingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты маппера для объекта Booking")
public class BookingMapperTest {

    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    private Booking booking;

    private BookingDTO bookingDTO;

    private final long BOOKING_ID = 1L;
    private final long USER_ID = 2L;
    private final long COWORKING_ID = 3L;
    private final LocalDate DATE = LocalDate.now();
    private final List<TimeSlot> TIME_SLOTS = List.of(TimeSlot.MORNING);

    @BeforeEach
    void setUp() {
        booking = new Booking(BOOKING_ID, USER_ID, COWORKING_ID, DATE, TIME_SLOTS);
        bookingDTO = new BookingDTO(BOOKING_ID, USER_ID, COWORKING_ID, DATE, TIME_SLOTS);
    }

    @Test
    @DisplayName("Тест маппинга объекта BookingDTO из объекта Booking")
    void shouldMapBookingToBookingDTO() {
        BookingDTO bookingDTO = bookingMapper.toBookingDTO(booking);
        assertThat(bookingDTO).isNotNull();
        assertThat(bookingDTO.id()).isEqualTo(BOOKING_ID);
        assertThat(bookingDTO.userId()).isEqualTo(USER_ID);
        assertThat(bookingDTO.coworkingId()).isEqualTo(COWORKING_ID);
        assertThat(bookingDTO.date()).isEqualTo(DATE);
        assertThat(bookingDTO.timeSlots()).hasSize(TIME_SLOTS.size());
        assertThat(bookingDTO.timeSlots().get(0)).isEqualTo(TIME_SLOTS.get(0));
    }

    @Test
    @DisplayName("Тест маппинга объекта Booking из объекта BookingDTO")
    void shouldMapBookingDTOToBooking() {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        assertThat(booking).isNotNull();
        assertThat(booking.id()).isEqualTo(BOOKING_ID);
        assertThat(booking.userId()).isEqualTo(USER_ID);
        assertThat(booking.coworkingId()).isEqualTo(COWORKING_ID);
        assertThat(booking.date()).isEqualTo(DATE);
        assertThat(booking.timeSlots()).hasSize(TIME_SLOTS.size());
        assertThat(booking.timeSlots().get(0)).isEqualTo(TIME_SLOTS.get(0));
    }

    @Test
    @DisplayName("Тест маппинга объекта Booking из объекта BookingDTO и вставка нового ID")
    void shouldMapBookingDTOToBookingWithId() {
        long id = 5L;

        Booking booking = bookingMapper.toBooking(bookingDTO, id);
        assertThat(booking).isNotNull();
        assertThat(booking.id()).isEqualTo(id);
        assertThat(booking.userId()).isEqualTo(USER_ID);
        assertThat(booking.coworkingId()).isEqualTo(COWORKING_ID);
        assertThat(booking.date()).isEqualTo(DATE);
        assertThat(booking.timeSlots()).isEqualTo(TIME_SLOTS);
    }
}

