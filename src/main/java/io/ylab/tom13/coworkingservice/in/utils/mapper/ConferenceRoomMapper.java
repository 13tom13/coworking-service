package io.ylab.tom13.coworkingservice.in.utils.mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Маппер для преобразования между объектами ConferenceRoom и ConferenceRoomDTO.
 */
@Mapper
public interface ConferenceRoomMapper {

    /**
     * Получение экземпляра маппера ConferenceRoomMapper.
     */
    ConferenceRoomMapper INSTANCE = Mappers.getMapper(ConferenceRoomMapper.class);

    /**
     * Преобразует объект ConferenceRoom в ConferenceRoomDTO.
     *
     * @param conferenceRoom объект ConferenceRoom для преобразования.
     * @return соответствующий объект ConferenceRoomDTO.
     */
    default ConferenceRoomDTO toConferenceRoomDTO(ConferenceRoom conferenceRoom) {
        if (conferenceRoom == null) {
            return null;
        }
        return new ConferenceRoomDTO(
                conferenceRoom.getId(),
                conferenceRoom.getName(),
                conferenceRoom.getDescription(),
                conferenceRoom.isAvailable(),
                conferenceRoom.getCapacity()
        );
    }

    /**
     * Преобразует объект ConferenceRoomDTO в ConferenceRoom.
     *
     * @param conferenceRoomDTO объект ConferenceRoomDTO для преобразования.
     * @return соответствующий объект ConferenceRoom.
     */
    default ConferenceRoom toConferenceRoom(ConferenceRoomDTO conferenceRoomDTO) {
        if (conferenceRoomDTO == null) {
            return null;
        }
        return new ConferenceRoom(
                conferenceRoomDTO.getId(),
                conferenceRoomDTO.getName(),
                conferenceRoomDTO.getDescription(),
                conferenceRoomDTO.isAvailable(),
                conferenceRoomDTO.getCapacity()
        );
    }

    /**
     * Преобразует объект ConferenceRoomDTO в ConferenceRoom с указанным идентификатором.
     *
     * @param conferenceRoomDTO объект ConferenceRoomDTO для преобразования.
     * @param id                идентификатор конференц-зала.
     * @return соответствующий объект ConferenceRoom с заданным идентификатором.
     */
    default ConferenceRoom toConferenceRoom(ConferenceRoomDTO conferenceRoomDTO, long id) {
        if (conferenceRoomDTO == null) {
            return null;
        }
        return new ConferenceRoom(
                id,
                conferenceRoomDTO.getName(),
                conferenceRoomDTO.getDescription(),
                conferenceRoomDTO.isAvailable(),
                conferenceRoomDTO.getCapacity()
        );
    }
}
