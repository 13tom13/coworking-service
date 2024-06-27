package io.ylab.tom13.coworkingservice.out.client;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.rest.controller.AuthorizationController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.AuthorizationControllerImpl;
import io.ylab.tom13.coworkingservice.out.exceptions.LoginException;

/**
 * Клиентский компонент для авторизации пользователей.
 * Реализует методы для взаимодействия с контроллером авторизации.
 */
public class AuthorizationClient extends Client {

    private final AuthorizationController controller;

    /**
     * Конструктор, инициализирующий клиентский компонент с контроллером авторизации по умолчанию.
     */
    public AuthorizationClient() {
        controller = new AuthorizationControllerImpl();
    }

    /**
     * Авторизация пользователя с использованием предоставленных данных.
     *
     * @param authorizationDTO данные для авторизации пользователя
     */
    public UserDTO login(AuthorizationDTO authorizationDTO) throws LoginException {
        ResponseDTO<UserDTO> response = controller.login(authorizationDTO);
        if (response.success()) {
           return response.data();
        } else {
            throw new LoginException(response.message());
        }
    }

}

