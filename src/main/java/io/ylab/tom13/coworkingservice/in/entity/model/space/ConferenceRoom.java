package io.ylab.tom13.coworkingservice.in.entity.model.space;

import lombok.Data;

@Data
public class ConferenceRoom extends Space {
    private final int capacity;

    public ConferenceRoom(long id, String name, String description, boolean available, int capacity) {
        super(id, name, description, available);
        this.capacity = capacity;
    }
}
