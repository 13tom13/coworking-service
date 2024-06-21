package io.ylab.tom13.coworkingservice.in.config;


import java.time.LocalTime;

public class BookingConfig {
    public static final LocalTime BOOKING_START_TIME = LocalTime.of(8, 0);
    public static final LocalTime BOOKING_END_TIME = LocalTime.of(20, 0);

    private BookingConfig() {

    }
}
