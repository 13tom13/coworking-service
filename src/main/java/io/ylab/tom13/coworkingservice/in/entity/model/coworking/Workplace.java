package io.ylab.tom13.coworkingservice.in.entity.model.coworking;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Класс Workplace представляет рабочее место как тип коворкинга.
 * Наследуется от абстрактного класса Coworking и добавляет атрибут типа рабочего места.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Workplace extends Coworking {

    /** Тип рабочего места */
    private final String type;

    /**
     * Конструктор для инициализации объекта рабочего места.
     *
     * @param id          уникальный идентификатор рабочего места
     * @param name        название рабочего места
     * @param description описание рабочего места
     * @param available   доступность рабочего места
     * @param type        тип рабочего места
     */
    public Workplace(long id, String name, String description, boolean available, String type) {
        super(id, name, description, available);
        this.type = type;
    }
}
