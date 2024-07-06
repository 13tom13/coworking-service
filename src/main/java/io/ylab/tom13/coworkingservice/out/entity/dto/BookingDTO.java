package io.ylab.tom13.coworkingservice.out.entity.dto;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
        @Min(value = 0, message = "ID должен быть не отрицательным числом")
        long id,

        @Min(value = 1, message = "ID пользователя должен быть положительным числом")
        long userId,

        @Min(value = 1, message = "ID коворкинга должен быть положительным числом")
        long coworkingId,

        @NotNull(message = "Дата не должна быть пустой")
        @FutureOrPresent(message = "Дата бронирования должна быть в настоящем или будущем")
        LocalDate date,

        @Valid
        @NotEmpty(message = "Список timeSlots не должен быть пустым")
        List<TimeSlot> timeSlots
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
