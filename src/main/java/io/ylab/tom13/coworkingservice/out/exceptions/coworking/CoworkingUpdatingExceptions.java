package io.ylab.tom13.coworkingservice.out.exceptions.coworking;

/**
 * Исключение, выбрасываемое при ошибке обновления коворкинга.
 * Это исключение используется для указания, что произошла ошибка при попытке изменить коворкинг.
 */
public class CoworkingUpdatingExceptions extends RuntimeException {

    /**
     * Конструктор для исключения с указанием сообщения.
     *
     * @param message сообщение об ошибке
     */
    public CoworkingUpdatingExceptions(String message) {
        super("Ошибка при изменении коворкинга: " + message);
    }
}