package io.ylab.tom13.coworkingservice.in.entity.dto.coworking;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO класс для конференц-залов, наследует CoworkingDTO.
 * Содержит дополнительный атрибут - вместимость зала.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ConferenceRoomDTO extends CoworkingDTO {
    /** Вместимость конференц-зала */
    private int capacity;

    /**
     * Конструктор для инициализации объекта конференц-зала.
     *
     * @param id уникальный идентификатор
     * @param name название
     * @param description описание
     * @param available доступность
     * @param capacity вместимость зала
     */
    public ConferenceRoomDTO(long id, String name, String description, boolean available, int capacity) {
        super(id, name, description, available);
        this.capacity = capacity;
    }

    /**
     * Представление объекта в виде строки.
     *
     * @return строковое представление объекта в формате "Название: {name} | Описание: {description} | Количество мест: {capacity}"
     */
    @Override
    public String toString() {
        return super.toString() + "Количество мест: " + capacity;
    }
}
