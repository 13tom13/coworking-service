package io.ylab.tom13.coworkingservice.in.rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;

import java.util.List;

/**
 * Интерфейс контроллера администрирования.
 * Определяет методы для выполнения административных операций с пользователями в системе.
 */
public interface AdministrationController {

    /**
     * Получить список всех пользователей системы.
     *
     * @return объект ResponseDTO со списком пользователей или сообщением об ошибке
     */
    ResponseDTO<List<UserDTO>> getAllUsers();

    /**
     * Получить пользователя по электронной почте.
     *
     * @param email электронная почта пользователя
     * @return объект ResponseDTO с данными пользователя или сообщением об ошибке
     */
    ResponseDTO<UserDTO> getUserByEmail(String email);

    /**
     * Редактировать данные пользователя администратором.
     *
     * @param userDTO объект с обновленными данными пользователя
     * @return объект ResponseDTO с обновленными данными пользователя или сообщением об ошибке
     */
    ResponseDTO<UserDTO> editUserByAdministrator(UserDTO userDTO);

    /**
     * Изменить пароль пользователя администратором.
     *
     * @param userId          идентификатор пользователя
     * @param newHashPassword новый зашифрованный пароль
     * @return объект ResponseDTO с сообщением об успешной смене пароля или сообщением об ошибке
     */
    ResponseDTO<String> editUserPasswordByAdministrator(long userId, String newHashPassword);

    /**
     * Зарегистрировать нового пользователя администратором.
     *
     * @param registrationDTO данные нового пользователя для регистрации
     * @param role            роль нового пользователя
     * @return объект ResponseDTO с сообщением об успешной регистрации или сообщением об ошибке
     */
    ResponseDTO<String> registrationUser(RegistrationDTO registrationDTO, Role role);
}

