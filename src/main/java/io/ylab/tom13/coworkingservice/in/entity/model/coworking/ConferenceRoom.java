package io.ylab.tom13.coworkingservice.in.entity.model.coworking;

import lombok.Data;

@Data
public class ConferenceRoom extends Coworking {
    private final int capacity;

    public ConferenceRoom(long id, String name, String description, boolean available, int capacity) {
        super(id, name, description, available);
        this.capacity = capacity;
    }
}
