package io.ylab.tom13.coworkingservice.out.exceptions.mapper;

/**
 * Исключение, которое выбрасывается при попытке выполнить маппинг объекта коворкинга,
 * если тип коворкинга неизвестен или не поддерживается.
 */
public class CoworkingMappingException extends IllegalArgumentException {

    /**
     * Конструктор для создания исключения с указанием неизвестного типа коворкинга.
     *
     * @param unknownType неизвестный тип коворкинга.
     */
    public CoworkingMappingException(String unknownType) {
        super("Неизвестный тип коворкинга: " + unknownType);
    }
}

