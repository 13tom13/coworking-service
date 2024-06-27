package io.ylab.tom13.coworkingservice.in.exceptions.booking;

/**
 * Исключение, выбрасываемое при отсутствии найденного бронирования.
 * Это исключение используется для указания, что запрошенное бронирование не найдено.
 */
public class BookingNotFoundException extends Exception {

    /**
     * Конструктор для исключения с указанием сообщения.
     *
     * @param message сообщение об ошибке
     */
    public BookingNotFoundException(String message) {
        super(message);
    }
}
