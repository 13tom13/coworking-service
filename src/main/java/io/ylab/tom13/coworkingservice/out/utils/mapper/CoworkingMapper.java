package io.ylab.tom13.coworkingservice.out.utils.mapper;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.out.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.out.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.out.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.out.exceptions.mapper.CoworkingMappingException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер для преобразования между объектами Coworking и их DTO.
 */

@Mapper
public interface CoworkingMapper {

    CoworkingMapper INSTANCE = Mappers.getMapper(CoworkingMapper.class);

    @Mapping(target = "type", ignore = true)
    ConferenceRoomDTO toConferenceRoomDTO(ConferenceRoom conferenceRoom);

    ConferenceRoom toConferenceRoom(ConferenceRoomDTO conferenceRoomDTO);

    @Mapping(target = "workplaceType", source = "type")
    WorkplaceDTO toWorkplaceDTO(Workplace workplace);

    @Mapping(target = "type", source = "workplaceType")
    Workplace toWorkplace(WorkplaceDTO workplaceDTO);

    default CoworkingDTO toCoworkingDTO(Coworking coworking) {
        if (coworking instanceof ConferenceRoom) {
            return toConferenceRoomDTO((ConferenceRoom) coworking);
        } else if (coworking instanceof Workplace) {
            return toWorkplaceDTO((Workplace) coworking);
        } else {
            throw new CoworkingMappingException("Unsupported type of Coworking: " + coworking.getClass().getSimpleName());
        }
    }

    default Coworking toCoworking(CoworkingDTO coworkingDTO) {
        if (coworkingDTO instanceof ConferenceRoomDTO) {
            return toConferenceRoom((ConferenceRoomDTO) coworkingDTO);
        } else if (coworkingDTO instanceof WorkplaceDTO) {
            return toWorkplace((WorkplaceDTO) coworkingDTO);
        } else {
            throw new CoworkingMappingException("Unsupported type of CoworkingDTO: " + coworkingDTO.getClass().getSimpleName());
        }
    }
}