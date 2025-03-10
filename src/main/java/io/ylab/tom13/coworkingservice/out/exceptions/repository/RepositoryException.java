package io.ylab.tom13.coworkingservice.out.exceptions.repository;

/**
 * Исключение, выбрасываемое при ошибке работы с репозиторием.
 * Это исключение используется для указания, что произошла ошибка при выполнении операций с репозиторием данных.
 */
public class RepositoryException extends RuntimeException {

    /**
     * Конструктор для исключения с указанием сообщения.
     *
     * @param message сообщение об ошибке
     */
    public RepositoryException(String message) {
        super("Ошибка репозитория: " + message);
    }
}
