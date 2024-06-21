package io.ylab.tom13.coworkingservice.in.exceptions.repository;

/**
 * Исключение, которое выбрасывается, если пользователь с указанным email уже существует.
 */
public class UserAlreadyExistsException extends RepositoryException {

    /**
     * Конструктор исключения с указанием email пользователя.
     *
     * @param email email пользователя, который уже существует
     */
    public UserAlreadyExistsException(String email) {
        super("Пользователь с email " + email + " уже существует");
    }
}
