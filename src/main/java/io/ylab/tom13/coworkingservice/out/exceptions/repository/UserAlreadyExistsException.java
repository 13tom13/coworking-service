package io.ylab.tom13.coworkingservice.out.exceptions.repository;

/**
 * Исключение, которое выбрасывается, если пользователь с указанным email уже существует.
 */
public class UserAlreadyExistsException extends Exception {

    /**
     * Конструктор исключения с указанием email пользователя.
     *
     * @param email email пользователя, который уже существует
     */
    public UserAlreadyExistsException(String email) {
        super("Пользователь с email " + email + " уже существует");
    }
}
