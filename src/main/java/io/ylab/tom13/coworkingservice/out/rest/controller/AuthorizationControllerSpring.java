package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер авторизации.
 * Обрабатывает запросы на аутентификацию пользователя и возвращает соответствующие результаты.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер авторизации", description = "Обрабатывает запросы на аутентификацию пользователя и возвращает соответствующие результаты.")
public class AuthorizationControllerSpring {

    private final AuthorizationService authorizationService;
    private final JwtUtil jwtUtil;

    /**
     * Метод для выполнения входа пользователя на основе предоставленных данных аутентификации.
     *
     * @param authorizationDTO данные аутентификации пользователя (email и пароль).
     * @return объект ResponseEntity, содержащий результат операции входа.
     * @throws UnauthorizedException если аутентификация не удалась
     */
    @Operation(summary = "Вход пользователя", description = "Аутентифицирует пользователя и возвращает JWT токен.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Parameter(description = "Данные для авторизации пользователя", required = true)
            @RequestBody AuthorizationDTO authorizationDTO) {
        UserDTO user = authorizationService.login(authorizationDTO);
        String token = jwtUtil.generateJwt(user.id(), user.role().name());
        return ResponseEntity.ok(token);
    }

    /**
     * Метод для выполнения выхода пользователя.
     *
     * @return объект ResponseEntity, с результатом операции выхода.
     */
    @Operation(summary = "Выход пользователя", description = "Выход пользователя из системы.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}