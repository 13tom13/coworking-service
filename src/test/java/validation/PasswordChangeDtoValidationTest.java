package validation;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

@DisplayName("Тестирование валидации PasswordChangeDTO")
public class PasswordChangeDtoValidationTest extends ValidationTest {

    @Test
    @DisplayName("Валидный PasswordChangeDTO")
    void testValidPasswordChangeDTO() {
        PasswordChangeDTO dto = new PasswordChangeDTO(
                "john.doe@example.com",
                "oldPassword123",
                "newPassword456"
        );

        Set<ConstraintViolation<PasswordChangeDTO>> violations = validator.validate(dto);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Невалидный PasswordChangeDTO с пустым email")
    void testInvalidPasswordChangeDTOWithEmptyEmail() {
        PasswordChangeDTO dto = new PasswordChangeDTO(
                "",
                "oldPassword123",
                "newPassword456"
        );

        Set<ConstraintViolation<PasswordChangeDTO>> violations = validator.validate(dto);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Email не должен быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Невалидный PasswordChangeDTO с невалидным форматом email")
    void testInvalidPasswordChangeDTOWithInvalidEmail() {
        PasswordChangeDTO dto = new PasswordChangeDTO(
                "invalid.email",
                "oldPassword123",
                "newPassword456"
        );

        Set<ConstraintViolation<PasswordChangeDTO>> violations = validator.validate(dto);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Неверный формат email", violations.iterator().next().getMessage());
    }
}
