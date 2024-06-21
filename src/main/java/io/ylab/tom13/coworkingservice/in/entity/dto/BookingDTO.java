package io.ylab.tom13.coworkingservice.in.entity.dto;

import java.time.LocalDateTime;

public record BookingDTO(
        long userId,
        long spaceId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
