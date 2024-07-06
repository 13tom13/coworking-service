package validation;


import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование валидации RegistrationDTO")
public class RegistrationDtoValidationTest extends ValidationTest {

    @Test
    @DisplayName("Валидный RegistrationDTO")
    void testValidRegistrationDTO() {
        RegistrationDTO dto = new RegistrationDTO("John", "Doe", "john.doe@example.com", "password123");

        Set<ConstraintViolation<RegistrationDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Невалидный RegistrationDTO с пустым именем")
    void testInvalidRegistrationDTOWithEmptyFirstName() {
        RegistrationDTO dto = new RegistrationDTO("", "Doe", "john.doe@example.com", "password123");

        Set<ConstraintViolation<RegistrationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Имя не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Невалидный RegistrationDTO с неверным форматом email")
    void testInvalidRegistrationDTOWithInvalidEmail() {
        RegistrationDTO dto = new RegistrationDTO("John", "Doe", "invalid.email", "password123", null);

        Set<ConstraintViolation<RegistrationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Неверный формат email", violations.iterator().next().getMessage());
    }
}
