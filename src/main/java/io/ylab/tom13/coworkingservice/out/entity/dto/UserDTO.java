package io.ylab.tom13.coworkingservice.out.entity.dto;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO представляющий пользователя.
 */
public record UserDTO(
        long id,
        @NotBlank(message = "Имя не должно быть пустым")
        String firstName,

        @NotBlank(message = "Фамилия не должна быть пустой")
        String lastName,

        @Email(message = "Некорректный формат email")
        String email,

        @NotNull(message = "Роль не может быть пустой")
        Role role
) {
    @Override
    public String toString() {
        return String.format("%s: %s %s (%s)", role.getDisplayName(), firstName, lastName, email);
    }
}
