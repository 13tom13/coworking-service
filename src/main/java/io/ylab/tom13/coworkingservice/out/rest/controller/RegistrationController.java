package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import org.springframework.http.ResponseEntity;

/**
 * Интерфейс контроллера регистрации пользователей.
 */
public interface RegistrationController {

    /**
     * Метод для создания нового пользователя на основе предоставленных данных регистрации.
     *
     * @param registrationDTO данные регистрации нового пользователя
     * @return объект ResponseEntity, содержащий результат операции создания пользователя
     */
    ResponseEntity<?> createUser(RegistrationDTO registrationDTO);
}

