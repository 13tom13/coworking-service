package io.ylab.tom13.coworkingservice.in.entity.dto.coworking;

import lombok.Data;

/**
 * DTO класс для рабочих мест, наследует CoworkingDTO.
 * Содержит дополнительный атрибут - тип рабочего места.
 */
@Data
public class WorkplaceDTO extends CoworkingDTO {
    /** Тип рабочего места */
    private String type;

    /**
     * Конструктор для инициализации объекта рабочего места.
     *
     * @param id уникальный идентификатор
     * @param name название
     * @param description описание
     * @param available доступность
     * @param type тип рабочего места
     */
    public WorkplaceDTO(long id, String name, String description, boolean available, String type) {
        super(id, name, description, available);
        this.type = type;
    }

    /**
     * Представление объекта в виде строки.
     *
     * @return строковое представление объекта в формате "Название: {name} | Описание: {description} | Тип: {type}"
     */
    @Override
    public String toString() {
        return super.toString() + "Тип: " + type;
    }
}