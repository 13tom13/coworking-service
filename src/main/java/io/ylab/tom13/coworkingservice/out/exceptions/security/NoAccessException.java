package io.ylab.tom13.coworkingservice.out.exceptions.security;

/**
 * Исключение, выбрасываемое при отказе в доступе.
 * Это исключение используется для указания, что операция не может быть выполнена из-за отсутствия доступа.
 */
public class NoAccessException extends Exception {

    /**
     * Конструктор для исключения с сообщением "Отказано в доступе".
     */
    public NoAccessException() {
        super("Отказано в доступе");
    }

}

