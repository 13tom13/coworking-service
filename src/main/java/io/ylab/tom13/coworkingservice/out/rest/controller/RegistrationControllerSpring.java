package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Контроллер для регистрации нового пользователя.
 * Обрабатывает запросы на создание нового пользователя и возвращает соответствующие результаты.
 */
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@Tag(name = "Контроллер регистрации", description = "Обрабатывает запросы на создание нового пользователя.")
public class RegistrationControllerSpring {

    private final RegistrationService registrationService;

    /**
     * Метод для создания нового пользователя на основе предоставленных данных регистрации.
     *
     * @param registrationDTO данные регистрации нового пользователя
     * @return объект ResponseEntity, содержащий результат операции создания пользователя
     */
    @Operation(summary = "Создание нового пользователя", description = "Создает нового пользователя и возвращает его данные.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<?> createUser(@Parameter(description = "Данные для регистрации пользователя", required = true) @RequestBody final RegistrationDTO registrationDTO) {
        UserDTO user = registrationService.createUser(registrationDTO);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(user);
    }
}