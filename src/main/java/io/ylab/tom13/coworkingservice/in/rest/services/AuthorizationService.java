package io.ylab.tom13.coworkingservice.in.rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;

/**
 * Сервис авторизации пользователей.
 */
public interface AuthorizationService {

    /**
     * Выполняет процесс авторизации пользователя на основе предоставленных данных.
     *
     * @param authorizationDTO данные для авторизации пользователя
     * @return DTO с данными авторизованного пользователя
     * @throws UnauthorizedException если пользователь не авторизован (неверный email или пароль)
     */
    UserDTO login(AuthorizationDTO authorizationDTO) throws UnauthorizedException;
}

