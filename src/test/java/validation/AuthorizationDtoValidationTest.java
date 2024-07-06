package validation;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

@DisplayName("Тестирование валидации AuthorizationDTO")
public class AuthorizationDtoValidationTest extends ValidationTest {

    @Test
    @DisplayName("Валидный AuthorizationDTO")
    void testValidAuthorizationDTO() {
        AuthorizationDTO dto = new AuthorizationDTO(
                "test@example.com",
                "password123"
        );

        Set<ConstraintViolation<AuthorizationDTO>> violations = validator.validate(dto);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Невалидный AuthorizationDTO с неверным email")
    void testInvalidAuthorizationDTOWithInvalidEmail() {
        AuthorizationDTO dto = new AuthorizationDTO(
                "invalid.email",
                "password123"
        );

        Set<ConstraintViolation<AuthorizationDTO>> violations = validator.validate(dto);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Неверный формат email", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Невалидный AuthorizationDTO с пустым паролем")
    void testInvalidAuthorizationDTOWithEmptyPassword() {
        AuthorizationDTO dto = new AuthorizationDTO(
                "test@example.com",
                ""
        );

        Set<ConstraintViolation<AuthorizationDTO>> violations = validator.validate(dto);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Пароль не должен быть пустым", violations.iterator().next().getMessage());
    }
}
