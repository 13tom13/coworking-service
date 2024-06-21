package io.ylab.tom13.coworkingservice.in.entity.model.space;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Space {
    protected final long id;
    protected final String name;
    protected final String description;
    protected final boolean available;
}
