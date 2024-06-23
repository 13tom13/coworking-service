package io.ylab.tom13.coworkingservice.in.entity.model.coworking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Coworking {
    protected final long id;
    protected final String name;
    protected final String description;
    protected final boolean available;
}
