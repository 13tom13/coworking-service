package io.ylab.tom13.coworkingservice.in.entity.model.space;

import lombok.Data;

@Data
public class Workplace extends Space {
    private final String type;

    public Workplace(long id, String name, String description, boolean available, String type) {
        super(id, name, description, available);
        this.type = type;
    }
}
