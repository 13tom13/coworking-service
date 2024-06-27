package io.ylab.tom13.coworkingservice.in.entity.model.coworking;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Абстрактный класс Coworking представляет собой базовую сущность коворкинга.
 * Содержит основные атрибуты: идентификатор, название, описание и статус доступности.
 */
@Data
@AllArgsConstructor
public abstract class Coworking {
    /** Уникальный идентификатор коворкинга */
    protected final long id;

    /** Название коворкинга */
    protected final String name;

    /** Описание коворкинга */
    protected final String description;

    /** Доступность коворкинга */
    protected final boolean available;
}
