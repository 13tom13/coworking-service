package io.ylab.tom13.coworkingservice.in.entity.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {
    private long id;
    private long userId;
    private long spaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
} 
