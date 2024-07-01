package io.ylab.tom13.coworkingservice.in.entity.dto.coworking;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Абстрактный класс для DTO коворкингов.
 * Содержит базовые атрибуты и методы для представления информации о коворкинге.
 */
@Data
@AllArgsConstructor
public abstract class CoworkingDTO {
    /**
     * Уникальный идентификатор коворкинга
     */
    private long id;
    /**
     * Название коворкинга
     */
    private String name;
    /**
     * Описание коворкинга
     */
    private String description;
    /**
     * Доступность коворкинга
     */
    private boolean available;

    /**
     * Представление объекта в виде строки.
     *
     * @return строковое представление объекта в формате "Название: {name} | Описание: {description} |"
     */
    @Override
    public String toString() {
        return String.format("Название: %s | Описание: %s |", name, description);
    }
}
