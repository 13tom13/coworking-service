package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;

/**
 * Интерфейс для управления операциями редактирования пользователей.
 */
public interface UserEditController {

    /**
     * Редактирует информацию о пользователе на основе предоставленного объекта UserDTO.
     *
     * @param userDTO DTO с информацией о пользователе для редактирования.
     * @return ResponseDTO с обновленным объектом UserDTO или сообщением об ошибке.
     */
    ResponseDTO<UserDTO> editUser(UserDTO userDTO);

    /**
     * Изменяет пароль пользователя на основе предоставленного объекта PasswordChangeDTO.
     *
     * @param passwordChangeDTO DTO с новым паролем и идентификатором пользователя.
     * @return ResponseDTO с сообщением об успешном изменении пароля или сообщением об ошибке.
     */
    ResponseDTO<String> editPassword(PasswordChangeDTO passwordChangeDTO);

}

