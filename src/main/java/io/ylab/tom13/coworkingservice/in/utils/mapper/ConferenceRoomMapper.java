package io.ylab.tom13.coworkingservice.in.utils.mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConferenceRoomMapper {
    ConferenceRoomMapper INSTANCE = Mappers.getMapper(ConferenceRoomMapper.class);

    default ConferenceRoomDTO toConferenceRoomDTO(ConferenceRoom conferenceRoom){
        return new ConferenceRoomDTO(conferenceRoom.getId(), conferenceRoom.getName(), conferenceRoom.getDescription(), conferenceRoom.isAvailable(), conferenceRoom.getCapacity());
    }

    default ConferenceRoom toConferenceRoom(ConferenceRoomDTO conferenceRoomDTO){
        return new ConferenceRoom(conferenceRoomDTO.getId(), conferenceRoomDTO.getName(), conferenceRoomDTO.getDescription(), conferenceRoomDTO.isAvailable(), conferenceRoomDTO.getCapacity());
    }

    default ConferenceRoom toConferenceRoom(ConferenceRoomDTO conferenceRoomDTO, long id) {
        if (conferenceRoomDTO == null) {
            return null;
        }
        return new ConferenceRoom(id, conferenceRoomDTO.getName(), conferenceRoomDTO.getDescription(), conferenceRoomDTO.isAvailable(), conferenceRoomDTO.getCapacity());
    }
}
