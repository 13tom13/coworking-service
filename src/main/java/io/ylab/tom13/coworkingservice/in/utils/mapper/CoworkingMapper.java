package io.ylab.tom13.coworkingservice.in.utils.mapper;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.exceptions.mapper.CoworkingMappingException;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Маппер для преобразования между объектами Coworking и их DTO.
 */

@Mapper
public interface CoworkingMapper {

    CoworkingMapper INSTANCE = Mappers.getMapper(CoworkingMapper.class);


    ConferenceRoomDTO toConferenceRoomDTO(ConferenceRoom conferenceRoom);


    ConferenceRoom toConferenceRoom(ConferenceRoomDTO conferenceRoomDTO);

    WorkplaceDTO toWorkplaceDTO(Workplace workplace);

    Workplace toWorkplace(WorkplaceDTO workplaceDTO);

    default CoworkingDTO toCoworkingDTO(Coworking coworking) {
        if (coworking instanceof ConferenceRoom) {
            return toConferenceRoomDTO((ConferenceRoom) coworking);
        } else if (coworking instanceof Workplace) {
            return toWorkplaceDTO((Workplace) coworking);
        } else {
            throw new CoworkingMappingException(coworking.getClass().getSimpleName());
        }
    }

    default Coworking toCoworking(CoworkingDTO coworkingDTO) {
        if (coworkingDTO instanceof ConferenceRoomDTO) {
            return toConferenceRoom((ConferenceRoomDTO) coworkingDTO);
        } else if (coworkingDTO instanceof WorkplaceDTO) {
            return toWorkplace((WorkplaceDTO) coworkingDTO);
        } else {
            throw new CoworkingMappingException(coworkingDTO.getClass().getSimpleName());
        }
    }
}