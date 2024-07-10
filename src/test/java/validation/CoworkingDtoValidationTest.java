package validation;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.WorkplaceDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

@DisplayName("Тестирование валидации CoworkingDTO, WorkplaceDTO и ConferenceRoomDTO")
public class CoworkingDtoValidationTest extends ValidationTest {


    @Test
    @DisplayName("Невалидная WorkplaceDTO с пустым именем")
    void testInvalidWorkplaceDTOWithEmptyName() {
        WorkplaceDTO dto = new WorkplaceDTO(1, "", "Description", true, "Desk");

        Set<ConstraintViolation<CoworkingDTO>> violations = validateAndGetViolations(dto);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Название коворкинга не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Валидация ConferenceRoomDTO")
    void testValidConferenceRoomDTO() {
        ConferenceRoomDTO dto = new ConferenceRoomDTO(1, "Room 1", "Description", true, 10);

        Set<ConstraintViolation<CoworkingDTO>> violations = validateAndGetViolations(dto);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Невалидная ConferenceRoomDTO с отрицательной вместимостью")
    void testInvalidConferenceRoomDTOWithNegativeCapacity() {
        ConferenceRoomDTO dto = new ConferenceRoomDTO(1, "Room 1", "Description", true, -5);

        Set<ConstraintViolation<CoworkingDTO>> violations = validateAndGetViolations(dto);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Вместимость зала должна быть больше 0", violations.iterator().next().getMessage());
    }

    private Set<ConstraintViolation<CoworkingDTO>> validateAndGetViolations(CoworkingDTO dto) {
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = factory.getValidator();
        return validator.validate(dto);
    }
}

