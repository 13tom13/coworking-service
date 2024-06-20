package io.ylab.tom13.coworkingservice.in.entity.dto;

/**
 * DTO для аутентификации пользователя.
 */
public record AuthorizationDTO (
        String email,
        String password
) {
}
