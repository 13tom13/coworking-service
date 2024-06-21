package io.ylab.tom13.coworkingservice.in.entity.dto.space;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class CoworkingDTO {
    private long id;
    private String name;
    private String description;
    private boolean available;

    @Override
    public String toString() {
        return  String.format("Название: %s | Описание: %s | ", name, description);
    }
}
