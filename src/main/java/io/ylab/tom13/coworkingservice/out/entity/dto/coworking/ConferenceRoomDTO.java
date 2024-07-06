package io.ylab.tom13.coworkingservice.out.entity.dto.coworking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO класс для конференц-залов, наследует CoworkingDTO.
 * Содержит дополнительный атрибут - вместимость зала.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConferenceRoomDTO extends CoworkingDTO {
    private String type = "conferenceRoom";
    @Positive(message = "Вместимость зала должна быть больше 0")
    private int capacity;

    @JsonCreator
    public ConferenceRoomDTO(@JsonProperty("id") long id,
                             @JsonProperty("name") String name,
                             @JsonProperty("description") String description,
                             @JsonProperty("available") boolean available,
                             @JsonProperty("capacity") int capacity) {
        super(id, name, description, available);
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return super.toString() + "Количество мест: " + capacity;
    }
}
