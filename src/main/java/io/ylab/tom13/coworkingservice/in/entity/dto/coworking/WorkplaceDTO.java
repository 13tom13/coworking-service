package io.ylab.tom13.coworkingservice.in.entity.dto.coworking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO класс для рабочих мест, наследует CoworkingDTO.
 * Содержит дополнительный атрибут - тип рабочего места.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkplaceDTO extends CoworkingDTO {
    private String type = "workplace";
    private String workplaceType;

    @JsonCreator
    public WorkplaceDTO(@JsonProperty("id") long id,
                        @JsonProperty("name") String name,
                        @JsonProperty("description") String description,
                        @JsonProperty("available") boolean available,
                        @JsonProperty("workplaceType") String workplaceType) {
        super(id, name, description, available);
        this.workplaceType = workplaceType;
    }

    @Override
    public String toString() {
        return super.toString() + "Тип: " + workplaceType;
    }
}