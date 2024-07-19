package io.ylab.tom13.coworkingservice.out.exceptions.coworking;

/**
 * Исключение, выбрасываемое при конфликте коворкинга.
 * Это исключение используется для указания, что произошел конфликт при создании с коворкингом.
 */
public class CoworkingConflictException extends RuntimeException {

    /**
     * Конструктор для исключения с указанием сообщения.
     *
     * @param message сообщение об ошибке
     */
    public CoworkingConflictException(String message)  {
        super(message);
    }
}
