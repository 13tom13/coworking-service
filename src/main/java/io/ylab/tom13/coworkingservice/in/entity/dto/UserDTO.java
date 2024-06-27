package io.ylab.tom13.coworkingservice.in.entity.dto;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;

/**
 * DTO представляющий пользователя.
 */
public record UserDTO(
        long id,
        String firstName,
        String lastName,
        String email,
        Role role
) {
    @Override
    public String toString() {
        return String.format("%s: %s %s (%s)", role.getDisplayName(), firstName, lastName, email);
    }
}
