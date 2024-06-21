package io.ylab.tom13.coworkingservice.in.entity.model;

import java.time.LocalTime;

public record TimeSlot(LocalTime startTime, LocalTime endTime) {
    @Override
    public String toString() {
        return String.format("Время:  %s - %s", startTime, endTime);
    }
}
