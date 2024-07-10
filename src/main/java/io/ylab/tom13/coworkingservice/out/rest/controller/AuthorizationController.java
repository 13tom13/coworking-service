package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import org.springframework.http.ResponseEntity;

/**
 * Интерфейс контроллера аутентификации пользователя.
 */
public interface AuthorizationController {

    /**
     * Метод для выполнения входа пользователя на основе предоставленных данных аутентификации.
     *
     * @param authorizationDTO данные аутентификации пользователя (email и пароль)
     * @return объект ResponseDTO, содержащий результат операции входа
     */
    ResponseEntity<?> login(AuthorizationDTO authorizationDTO);

    ResponseEntity<?> logout();
}

