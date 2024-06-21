package io.ylab.tom13.coworkingservice.in.entity.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {
    private final long id;
    private final long userId;
    private final long coworkingId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
} 
