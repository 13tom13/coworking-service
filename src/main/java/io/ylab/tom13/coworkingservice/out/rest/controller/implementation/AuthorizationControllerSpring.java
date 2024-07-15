package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.AuthorizationController;
import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Реализация интерфейса {@link AuthorizationController}.
 * Обрабатывает запросы на аутентификацию пользователя и возвращает соответствующие результаты.
 */
@RestController
@Tag(name = "Контроллер авторизации", description = "Обрабатывает запросы на аутентификацию пользователя и возвращает соответствующие результаты.")
public class AuthorizationControllerSpring implements AuthorizationController {

    private final AuthorizationService authorizationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthorizationControllerSpring(AuthorizationService authorizationService, JwtUtil jwtUtil) {
        this.authorizationService = authorizationService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * {@inheritDoc}
     */
    @Operation(summary = "Вход пользователя", description = "Аутентифицирует пользователя и возвращает JWT токен.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    @PostMapping("/login")
    @Override
    public ResponseEntity<?> login(
            @Parameter(description = "Данные для авторизации пользователя", required = true)
            @RequestBody AuthorizationDTO authorizationDTO) {
        try {
            UserDTO user = authorizationService.login(authorizationDTO);
            String token = jwtUtil.generateJwt(user.id(), user.role().name());
            return ResponseEntity.ok(token);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Operation(summary = "Выход пользователя", description = "Выход пользователя из системы.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    @GetMapping("/logout")
    @Override
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("До свидания");
    }
}