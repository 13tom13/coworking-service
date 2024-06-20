package io.ylab.tom13.coworkingservice.in.rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;

/**
 * Интерфейс контроллера регистрации пользователей.
 */
public interface RegistrationController {

    /**
     * Метод для создания нового пользователя на основе предоставленных данных регистрации.
     *
     * @param registrationDTO данные регистрации нового пользователя
     * @return объект ResponseDTO, содержащий результат операции создания пользователя
     */
    ResponseDTO<UserDTO> createUser(RegistrationDTO registrationDTO);
}

