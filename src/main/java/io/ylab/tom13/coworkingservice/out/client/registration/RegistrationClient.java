package io.ylab.tom13.coworkingservice.out.client.registration;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.rest.controller.RegistrationController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.RegistrationControllerImpl;
import io.ylab.tom13.coworkingservice.out.client.Client;
import io.ylab.tom13.coworkingservice.out.exceptions.RegistrationException;

/**
 * Клиентский компонент для регистрации пользователей.
 * Реализует методы для взаимодействия с контроллером регистрации.
 */
public class RegistrationClient extends Client {

    private final RegistrationController controller;

    /**
     * Конструктор, инициализирующий клиентский компонент с контроллером регистрации по умолчанию.
     */
    public RegistrationClient() {
        controller = new RegistrationControllerImpl();
    }

    /**
     * Регистрация нового пользователя с использованием предоставленных данных.
     *
     * @param registrationDTO данные пользователя для регистрации
     */
    public void registration(final RegistrationDTO registrationDTO) throws RegistrationException {
        ResponseDTO<UserDTO> response = controller.createUser(registrationDTO);
        if (response.success()) {
            UserDTO user = response.data();
            System.out.printf("Пользователь с именем: %s %s и email: %s успешно зарегистрирован%n",
                    user.firstName(), user.lastName(), user.email());
        } else {
            throw new RegistrationException(response.message());
        }
    }

}

