package io.ylab.tom13.coworkingservice.in.entity.enumeration;


import lombok.Getter;

import java.time.LocalTime;

/**
 * Перечисление временных слотов.
 * Каждый временной слот определяется начальным и конечным временем суток,
 * а также отображаемым наименованием.
 */
@Getter
public enum TimeSlot {

    /**
     * Утренний временной слот (с 8:00 до 12:00).
     */
    MORNING(LocalTime.of(8, 0), LocalTime.of(12, 0), "Утро"),

    /**
     * Дневной временной слот (с 12:00 до 16:00).
     */
    AFTERNOON(LocalTime.of(12, 0), LocalTime.of(16, 0), "День"),

    /**
     * Вечерний временной слот (с 16:00 до 20:00).
     */
    EVENING(LocalTime.of(16, 0), LocalTime.of(20, 0), "Вечер");

    /**
     * Начальное время временного слота.
     */
    private final LocalTime start;

    /**
     * Конечное время временного слота.
     */
    private final LocalTime end;

    /**
     * Отображаемое наименование временного слота.
     */
    private final String name;

    /**
     * Конструктор для создания временного слота с указанными параметрами.
     *
     * @param start начальное время временного слота
     * @param end   конечное время временного слота
     * @param name  отображаемое наименование временного слота
     */
    TimeSlot(LocalTime start, LocalTime end, String name) {
        this.start = start;
        this.end = end;
        this.name = name;
    }

    /**
     * Представление временного слота в виде строки.
     *
     * @return строковое представление временного слота в формате "{name} ({start} - {end})"
     */
    @Override
    public String toString() {
        return String.format("%s (%s - %s)", name, start, end);
    }
}

