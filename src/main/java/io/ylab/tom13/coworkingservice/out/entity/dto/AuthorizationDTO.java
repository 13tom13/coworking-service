package io.ylab.tom13.coworkingservice.out.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Класс DTO для представления учетных данных авторизации.
 * Этот record содержит поля для электронной почты и пароля.
 */
public record AuthorizationDTO(
        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Неверный формат email")
        String email,

        @NotBlank(message = "Пароль не должен быть пустым")
        String password
) {
}
