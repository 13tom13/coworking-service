package io.ylab.tom13.coworkingservice.in.entity.dto;

/**
 * DTO представляющий пользователя.
 */
public record UserDTO(
        long id,
        String firstName,
        String lastName,
        String email
) {
    @Override
    public String toString() {
        return String.format("%s %s (%s)", firstName, lastName, email);
    }
}
