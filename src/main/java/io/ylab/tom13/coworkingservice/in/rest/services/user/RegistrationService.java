package io.ylab.tom13.coworkingservice.in.rest.services.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;

/**
 * Сервис для регистрации пользователей.
 */
public interface RegistrationService {

    /**
     * Создает нового пользователя на основе переданных данных.
     *
     * @param registrationDTO данные пользователя для регистрации
     * @return DTO с данными созданного пользователя
     * @throws UserAlreadyExistsException если пользователь с таким email уже существует
     */
    UserDTO createUser(RegistrationDTO registrationDTO) throws UserAlreadyExistsException;
}

