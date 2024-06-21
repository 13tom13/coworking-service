package io.ylab.tom13.coworkingservice.in.entity.dto.space;

import lombok.Data;

@Data
public class ConferenceRoomDTO extends CoworkingDTO {
    private int capacity;

    public ConferenceRoomDTO(long id, String name, String description, boolean available, int capacity) {
        super(id, name, description, available);
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return super.toString() + "Количество мест: " + capacity;
    }
}
