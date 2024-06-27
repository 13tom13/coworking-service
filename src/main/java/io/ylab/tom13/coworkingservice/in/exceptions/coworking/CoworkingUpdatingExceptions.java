package io.ylab.tom13.coworkingservice.in.exceptions.coworking;

/**
 * Исключение, выбрасываемое при ошибке обновления коворкинга.
 * Это исключение используется для указания, что произошла ошибка при попытке изменить коворкинг.
 */
public class CoworkingUpdatingExceptions extends Exception {

    /**
     * Конструктор для исключения с указанием сообщения.
     *
     * @param message сообщение об ошибке
     */
    public CoworkingUpdatingExceptions(String message) {
        super("Ошибка при изменении коворкинга: " + message);
    }
}