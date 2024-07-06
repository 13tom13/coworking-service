package io.ylab.tom13.coworkingservice.out.entity.dto;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * DTO для регистрации нового пользователя.
 */

public record RegistrationDTO(
        @NotBlank(message = "Имя не должно быть пустым")
        String firstName,

        @NotBlank(message = "Фамилия не должна быть пустой")
        String lastName,

        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Неверный формат email")
        String email,

        @NotBlank(message = "Пароль не должен быть пустым")
        @Size(min = 4, message = "Пароль должен содержать как минимум 4 символа")
        String password,

        Role role
) {
    public RegistrationDTO(String firstName, String lastName, String email, String password) {
        this(firstName, lastName, email, password, Role.USER);
    }
}

