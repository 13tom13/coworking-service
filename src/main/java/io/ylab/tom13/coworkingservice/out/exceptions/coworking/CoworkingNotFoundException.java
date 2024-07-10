package io.ylab.tom13.coworkingservice.out.exceptions.coworking;

/**
 * Исключение, выбрасываемое при отсутствии найденного коворкинга.
 * Это исключение используется для указания, что запрашиваемый коворкинг не найден.
 */
public class CoworkingNotFoundException extends Exception {

    /**
     * Конструктор для исключения с указанием сообщения.
     *
     * @param message сообщение об ошибке
     */
    public CoworkingNotFoundException(String message) {
        super(message);
    }
}