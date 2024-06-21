package io.ylab.tom13.coworkingservice.in.entity.model;

import java.time.LocalDate;
import java.util.List;

public record Booking(
        long id,
        long userId,
        long coworkingId,
        LocalDate date,
        List<TimeSlot> timeSlots) {
}
