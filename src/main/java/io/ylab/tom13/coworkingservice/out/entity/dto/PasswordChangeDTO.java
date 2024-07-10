package io.ylab.tom13.coworkingservice.out.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для изменения пароля.
 */
public record PasswordChangeDTO(
        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Неверный формат email")
        String email,

        @NotBlank(message = "Старый пароль не должен быть пустым")
        String oldPassword,

        @NotBlank(message = "Новый пароль не должен быть пустым")
        @Size(min = 4, message = "Длина нового пароля должна быть не менее 4 символов")
        String newPassword
) {
}
