package io.ylab.tom13.coworkingservice.in.entity.dto.space;

import lombok.Data;

@Data
public class WorkplaceDTO extends CoworkingDTO {
    private String type;

    public WorkplaceDTO(long id, String name, String description, boolean available, String type) {
        super(id, name, description, available);
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString() + "Тип: " + type;
    }
}
