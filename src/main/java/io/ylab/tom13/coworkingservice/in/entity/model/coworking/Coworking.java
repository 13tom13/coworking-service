package io.ylab.tom13.coworkingservice.in.entity.model.coworking;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Абстрактный класс для представления коворкинга.
 * Содержит базовые атрибуты и методы для работы с коворкингом.
 */
@Data
@AllArgsConstructor
public abstract class Coworking {
    /** Уникальный идентификатор коворкинга */
    private final long id;

    /** Название коворкинга */
    private final String name;

    /** Описание коворкинга */
    private final String description;

    /** Доступность коворкинга */
    private final boolean available;

}
