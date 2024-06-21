package io.ylab.tom13.coworkingservice.in.entity.model;


import java.time.LocalTime;

public enum TimeSlot {

    MORNING(LocalTime.of(8, 0), LocalTime.of(12, 0), "Утро"),
    AFTERNOON(LocalTime.of(12, 0), LocalTime.of(16, 0), "День"),
    EVENING(LocalTime.of(16, 0), LocalTime.of(20, 0), "Вечер");

    private final LocalTime start;
    private final LocalTime end;
    private final String name;

    TimeSlot(LocalTime start, LocalTime end, String name) {
        this.start = start;
        this.end = end;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s (%s - %s)", name, start, end);
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }
}

