package io.ylab.tom13.coworkingservice.in.rest.controller.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;

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
    ResponseDTO<UserDTO> login(AuthorizationDTO authorizationDTO);
}

