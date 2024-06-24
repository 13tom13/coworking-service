package io.ylab.tom13.coworkingservice.in.entity.dto;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
public record BookingDTO(long id, long userId, long coworkingId, LocalDate date, @Singular List<TimeSlot> timeSlots) {
    public BookingDTO(long id, long userId, long coworkingId, LocalDate date, List<TimeSlot> timeSlots) {
        this.id = id;
        this.userId = userId;
        this.coworkingId = coworkingId;
        this.date = date;
        this.timeSlots = timeSlots == null ? List.of() : List.copyOf(timeSlots);
    }
}
