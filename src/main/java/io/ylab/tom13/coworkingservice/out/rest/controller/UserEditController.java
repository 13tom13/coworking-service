package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import org.springframework.http.ResponseEntity;

/**
 * Интерфейс для управления операциями редактирования пользователей.
 */
public interface UserEditController {

    /**
     * Редактирует информацию о пользователе на основе предоставленного объекта UserDTO.
     *
     * @param userDTO DTO с информацией о пользователе для редактирования.
     * @return ResponseEntity с обновленным объектом UserDTO или сообщением об ошибке.
     */
    ResponseEntity<?> editUser(UserDTO userDTO);

    /**
     * Изменяет пароль пользователя на основе предоставленного объекта PasswordChangeDTO.
     *
     * @param passwordChangeDTO DTO с новым паролем и идентификатором пользователя.
     * @return ResponseEntity с сообщением об успешном изменении пароля или сообщением об ошибке.
     */
    ResponseEntity<?> editPassword(PasswordChangeDTO passwordChangeDTO);

}

