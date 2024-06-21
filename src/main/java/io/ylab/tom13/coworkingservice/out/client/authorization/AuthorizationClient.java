package io.ylab.tom13.coworkingservice.out.client.authorization;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.rest.controller.AuthorizationController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.AuthorizationControllerImpl;
import io.ylab.tom13.coworkingservice.out.client.Client;
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
    public void login(AuthorizationDTO authorizationDTO) throws LoginException {
        ResponseDTO<UserDTO> response = controller.login(authorizationDTO);
        if (response.success()) {
            UserDTO user = response.data();
            localSession.setAttribute("user", user);
        } else {
            throw new LoginException(response.message());
        }
    }

}

