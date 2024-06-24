package io.ylab.tom13.coworkingservice.in.entity.dto;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;

/**
 * DTO для регистрации нового пользователя.
 */
public record RegistrationDTO(
        String firstName,
        String lastName,
        String email,
        String password,
        Role role
) {
}

