package io.ylab.tom13.coworkingservice.out.exceptions.booking;

/**
 * Исключение, выбрасываемое при конфликте бронирования.
 * Это исключение используется для указания, что новое бронирование
 * конфликтует с уже существующими бронированиями.
 */
public class BookingConflictException extends Exception {

    /**
     * Конструктор для исключения с указанием сообщения.
     *
     * @param message сообщение об ошибке
     */
    public BookingConflictException(String message)  {
        super(message);
    }
}