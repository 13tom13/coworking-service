package io.ylab.tom13.coworkingservice.out.client.authorization;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.rest.controller.AuthorizationController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.AuthorizationControllerImpl;
import io.ylab.tom13.coworkingservice.out.client.Client;

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
    public void login(AuthorizationDTO authorizationDTO) {
        ResponseDTO<UserDTO> response = controller.login(authorizationDTO);
        if (response.success()) {
            UserDTO user = response.data();
            localSession.setAttribute("user", user);
            System.out.printf("Пользователь с именем: %s %s и email: %s найден%n",
                    user.firstName(), user.lastName(), user.email());
        } else {
            System.err.printf("Ошибка авторизации: %s%n", response.message());
        }
    }

}

