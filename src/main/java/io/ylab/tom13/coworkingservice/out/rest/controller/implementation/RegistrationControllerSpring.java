package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.rest.controller.RegistrationController;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.ylab.tom13.coworkingservice.out.security.PasswordUtil.hashPassword;


/**
 * Реализация интерфейса {@link RegistrationController}.
 * Обрабатывает запросы на создание нового пользователя и возвращает соответствующие результаты.
 */
@RestController
@RequestMapping("/registration")
@Tag(name = "Контроллер регистрации", description = "Обрабатывает запросы на создание нового пользователя.")
public class RegistrationControllerSpring implements RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationControllerSpring(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Создание нового пользователя", description = "Создает нового пользователя и возвращает его данные.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<?> createUser(@Parameter(description = "Данные для регистрации пользователя", required = true) @RequestBody final RegistrationDTO registrationDTO) {
        try {
            String hashPassword = hashPassword(registrationDTO.password());
            RegistrationDTO hashedPassword = new RegistrationDTO(
                    registrationDTO.firstName(),
                    registrationDTO.lastName(),
                    registrationDTO.email(),
                    hashPassword,
                    registrationDTO.role()
            );
            UserDTO user = registrationService.createUser(hashedPassword);
            return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(user);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}