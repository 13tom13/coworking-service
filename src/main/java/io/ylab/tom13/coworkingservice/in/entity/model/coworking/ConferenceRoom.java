package io.ylab.tom13.coworkingservice.in.entity.model.coworking;

import lombok.Data;

/**
 * Класс ConferenceRoom представляет конференц-зал как тип коворкинга.
 * Наследуется от абстрактного класса Coworking и добавляет атрибут вместимости зала.
 */
@Data
public class ConferenceRoom extends Coworking {
    /** Вместимость конференц-зала */
    private final int capacity;

    /**
     * Конструктор для инициализации объекта конференц-зала.
     *
     * @param id          уникальный идентификатор
     * @param name        название
     * @param description описание
     * @param available   доступность
     * @param capacity    вместимость зала
     */
    public ConferenceRoom(long id, String name, String description, boolean available, int capacity) {
        super(id, name, description, available);
        this.capacity = capacity;
    }
}
