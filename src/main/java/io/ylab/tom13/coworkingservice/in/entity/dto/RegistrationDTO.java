package io.ylab.tom13.coworkingservice.in.entity.dto;

/**
 * DTO для регистрации нового пользователя.
 */
public record RegistrationDTO(
        String firstName,
        String lastName,
        String email,
        String password
) {
}

