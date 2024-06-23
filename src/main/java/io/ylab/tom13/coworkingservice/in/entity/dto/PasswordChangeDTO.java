package io.ylab.tom13.coworkingservice.in.entity.dto;

/**
 * DTO для изменения пароля.
 */
public record PasswordChangeDTO(
        String email,
        String oldPassword,
        String newPassword
) {
}
