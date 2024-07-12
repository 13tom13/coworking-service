package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import org.springframework.http.ResponseEntity;

/**
 * Интерфейс контроллера администрирования.
 * Определяет методы для выполнения административных операций с пользователями в системе.
 */
public interface AdministrationController {

    /**
     * Получить список всех пользователей системы.
     *
     * @return объект ResponseEntity со списком пользователей или сообщением об ошибке
     */
    ResponseEntity<?> getAllUsers();

    /**
     * Получить пользователя по электронной почте.
     *
     * @param email электронная почта пользователя
     * @return объект ResponseEntity с данными пользователя или сообщением об ошибке
     */
    ResponseEntity<?> getUserByEmail(String email);

    /**
     * Редактировать данные пользователя администратором.
     *
     * @param userDTO объект с обновленными данными пользователя
     * @return объект ResponseEntity с обновленными данными пользователя или сообщением об ошибке
     */
    ResponseEntity<?> editUserByAdministrator(UserDTO userDTO);

    /**
     * Изменить пароль пользователя администратором.
     *
     * @param passwordChangeDTO DTO с новым паролем и идентификатором пользователя.
     * @return объект ResponseEntity с сообщением об успешной смене пароля или сообщением об ошибке
     */
    ResponseEntity<?> editUserPasswordByAdministrator(PasswordChangeDTO passwordChangeDTO);

    /**
     * Зарегистрировать нового пользователя администратором.
     *
     * @param registrationDTO данные нового пользователя для регистрации
     * @return объект ResponseEntity с сообщением об успешной регистрации или сообщением об ошибке
     */
    ResponseEntity<?> registrationUser(RegistrationDTO registrationDTO);
}

