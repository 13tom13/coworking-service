package io.ylab.tom13.coworkingservice.out.rest.services;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;

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

