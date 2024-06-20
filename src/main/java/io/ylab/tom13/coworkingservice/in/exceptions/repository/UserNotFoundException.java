package io.ylab.tom13.coworkingservice.in.exceptions.repository;

/**
 * Исключение, которое выбрасывается, если пользователь с указанным email не найден.
 */
public class UserNotFoundException extends Exception {

    /**
     * Конструктор исключения с указанием email пользователя.
     *
     * @param email email пользователя, который не найден
     */
    public UserNotFoundException(String email) {
        super("Пользователь с email " + email + " не найден");
    }
}
