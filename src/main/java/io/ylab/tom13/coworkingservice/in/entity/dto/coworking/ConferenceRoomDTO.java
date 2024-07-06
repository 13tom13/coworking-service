package io.ylab.tom13.coworkingservice.in.entity.dto.coworking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
