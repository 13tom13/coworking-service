package io.ylab.tom13.coworkingservice.in.entity.dto.coworking;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Абстрактный класс для DTO коворкингов.
 * Содержит базовые атрибуты и методы для представления информации о коворкинге.
 */
@Data
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WorkplaceDTO.class, name = "workplace"),
        @JsonSubTypes.Type(value = ConferenceRoomDTO.class, name = "conferenceRoom")
})
public abstract class CoworkingDTO {
    private long id;
    private String name;
    private String description;
    private boolean available;

    @Override
    public String toString() {
        return String.format("Название: %s | Описание: %s |", name, description);
    }
}
