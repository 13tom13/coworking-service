package io.ylab.tom13.coworkingservice.in.entity.dto;

/**
 * Класс DTO для представления учетных данных авторизации.
 * Этот record содержит поля для электронной почты и пароля.
 */
public record AuthorizationDTO(String email, String password) {
}
