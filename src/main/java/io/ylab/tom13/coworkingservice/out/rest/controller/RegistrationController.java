package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import org.springframework.http.ResponseEntity;

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
    ResponseEntity<?> createUser(RegistrationDTO registrationDTO);
}

