package validation;


import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;


@DisplayName("Тестирование валидации UserDTO")
public class UserDtoValidationTest extends ValidationTest {

    @Test
    @DisplayName("Валидный пользователь")
    public void testValidUserDto() {
        UserDTO userDTO = new UserDTO(
                1,
                "John",
                "Doe",
                "john.doe@example.com",
                Role.USER
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Невалидный пользователь")
    public void testInvalidUserDto() {
        UserDTO userDTO = new UserDTO(
                1,
                "",
                "Doe",
                "invalid.email",
                null
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        Assertions.assertEquals(3, violations.size());
    }
}