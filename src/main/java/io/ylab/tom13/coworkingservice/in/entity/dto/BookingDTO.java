package io.ylab.tom13.coworkingservice.in.entity.dto;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO класс для представления данных о бронировании.
 * Этот record содержит идентификатор бронирования, идентификатор пользователя,
 * идентификатор коворкинга, дату бронирования и список временных слотов.
 */
@Builder
public record BookingDTO(
        long id,
        long userId,
        long coworkingId,
        LocalDate date,
        @Singular List<TimeSlot> timeSlots
) {
    /**
     * Конструктор для создания объекта BookingDTO.
     *
     * @param id          уникальный идентификатор бронирования
     * @param userId      идентификатор пользователя
     * @param coworkingId идентификатор коворкинга
     * @param date        дата бронирования
     * @param timeSlots   список временных слотов для бронирования
     */
    public BookingDTO(long id, long userId, long coworkingId, LocalDate date, List<TimeSlot> timeSlots) {
        this.id = id;
        this.userId = userId;
        this.coworkingId = coworkingId;
        this.date = date;
        this.timeSlots = timeSlots == null ? List.of() : List.copyOf(timeSlots);
    }
}
