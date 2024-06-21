package io.ylab.tom13.coworkingservice.in.entity.dto;

import java.time.LocalDateTime;

public record BookingDTO(
        long id,
        long userId,
        long coworkingId,
        LocalDateTime startTime,
        LocalDateTime endTime) {
}
