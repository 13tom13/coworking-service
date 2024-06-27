package io.ylab.tom13.coworkingservice.in.exceptions.repository;

/**
 * Исключение, которое выбрасывается, если пользователь с указанным email не найден.
 */
public class UserNotFoundException extends Exception {

    /**
     * Конструктор исключения с указанием email пользователя.
     *
     * @param message пояснения по исключению
     */
    public UserNotFoundException(String message) {
        super("Пользователь " + message + " не найден");
    }
}
