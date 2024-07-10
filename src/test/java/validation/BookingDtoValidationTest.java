package validation;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@DisplayName("Тестирование валидации BookingDTO")
public class BookingDtoValidationTest extends ValidationTest {


    @Test
    @DisplayName("Валидный BookingDTO")
    void testValidBookingDTO() {
        BookingDTO dto = new BookingDTO(
                1,
                1,
                1,
                LocalDate.now().plusDays(1),
                List.of(TimeSlot.MORNING)
        );

        Set<ConstraintViolation<BookingDTO>> violations = validator.validate(dto);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Невалидный BookingDTO с неправильной датой")
    void testInvalidBookingDTOWithInvalidDate() {
        BookingDTO dto = new BookingDTO(
                1,
                1,
                1,
                LocalDate.now().minusDays(1),
                List.of(TimeSlot.MORNING)
        );

        Set<ConstraintViolation<BookingDTO>> violations = validator.validate(dto);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Дата бронирования должна быть в настоящем или будущем", violations.iterator().next().getMessage());
    }


    @Test
    @DisplayName("Невалидный BookingDTO с пустым списком timeSlots")
    void testInvalidBookingDTOWithEmptyTimeSlots() {
        BookingDTO dto = new BookingDTO(
                1,
                1,
                1,
                LocalDate.now().plusDays(1), // будущая дата
                List.of()
        );

        Set<ConstraintViolation<BookingDTO>> violations = validator.validate(dto);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Список timeSlots не должен быть пустым", violations.iterator().next().getMessage());
    }
}
