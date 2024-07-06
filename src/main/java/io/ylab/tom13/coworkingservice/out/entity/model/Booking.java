package io.ylab.tom13.coworkingservice.out.entity.model;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;

import java.time.LocalDate;
import java.util.List;

/**
 * Запись о бронировании.
 * Этот record содержит идентификатор бронирования, идентификатор пользователя,
 * идентификатор коворкинга, дату бронирования и список временных слотов.
 */
public record Booking(
        /**
         * Уникальный идентификатор бронирования.
         */
        long id,

        /**
         * Идентификатор пользователя, сделавшего бронирование.
         */
        long userId,

        /**
         * Идентификатор коворкинга, который был забронирован.
         */
        long coworkingId,

        /**
         * Дата бронирования.
         */
        LocalDate date,

        /**
         * Список временных слотов для бронирования.
         */
        List<TimeSlot> timeSlots
) {
}