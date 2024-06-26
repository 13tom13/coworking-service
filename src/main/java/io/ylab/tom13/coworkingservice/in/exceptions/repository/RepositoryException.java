package io.ylab.tom13.coworkingservice.in.exceptions.repository;

public class RepositoryException extends Exception {

    /**
     * Конструктор исключения с указанием сообщения ошибки.
     *
     * @param message сообщение ошибки
     */
    public RepositoryException(String message) {
        super("Ошибка репозитория: " + message);
    }
}
