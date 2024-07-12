package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import org.springframework.http.ResponseEntity;

/**
 * Интерфейс контроллера аутентификации пользователя.
 */
public interface AuthorizationController {

    /**
     * Метод для выполнения входа пользователя на основе предоставленных данных аутентификации.
     *
     * @param authorizationDTO данные аутентификации пользователя (email и пароль)
     * @return объект ResponseEntity, содержащий результат операции входа
     */
    ResponseEntity<?> login(AuthorizationDTO authorizationDTO);

    ResponseEntity<?> logout();
}

