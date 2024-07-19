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

/**
 * Маппер для преобразования объектов Coworking и его наследников в соответствующие DTO и наоборот.
 */
@Mapper(componentModel = "spring")
public interface CoworkingMapper {

    /**
     * Преобразует объект ConferenceRoom в ConferenceRoomDTO.
     *
     * @param conferenceRoom объект ConferenceRoom
     * @return соответствующий ConferenceRoomDTO
     */
    @Mapping(target = "type", ignore = true)
    ConferenceRoomDTO toConferenceRoomDTO(ConferenceRoom conferenceRoom);

    /**
     * Преобразует объект ConferenceRoomDTO в ConferenceRoom.
     *
     * @param conferenceRoomDTO объект ConferenceRoomDTO
     * @return соответствующий ConferenceRoom
     */
    ConferenceRoom toConferenceRoom(ConferenceRoomDTO conferenceRoomDTO);

    /**
     * Преобразует объект Workplace в WorkplaceDTO.
     *
     * @param workplace объект Workplace
     * @return соответствующий WorkplaceDTO
     */
    @Mapping(target = "workplaceType", source = "type")
    WorkplaceDTO toWorkplaceDTO(Workplace workplace);

    /**
     * Преобразует объект WorkplaceDTO в Workplace.
     *
     * @param workplaceDTO объект WorkplaceDTO
     * @return соответствующий Workplace
     */
    @Mapping(target = "type", source = "workplaceType")
    Workplace toWorkplace(WorkplaceDTO workplaceDTO);

    /**
     * Преобразует объект Coworking в соответствующий DTO.
     * Если объект Coworking является экземпляром ConferenceRoom, используется toConferenceRoomDTO.
     * Если объект Coworking является экземпляром Workplace, используется toWorkplaceDTO.
     *
     * @param coworking объект Coworking для преобразования
     * @return соответствующий DTO
     * @throws CoworkingMappingException если тип Coworking не поддерживается
     */
    default CoworkingDTO toCoworkingDTO(Coworking coworking) {
        if (coworking instanceof ConferenceRoom) {
            return toConferenceRoomDTO((ConferenceRoom) coworking);
        } else if (coworking instanceof Workplace) {
            return toWorkplaceDTO((Workplace) coworking);
        } else {
            throw new CoworkingMappingException("Unsupported type of Coworking: " + coworking.getClass().getSimpleName());
        }
    }

    /**
     * Преобразует объект CoworkingDTO в соответствующий Coworking.
     * Если объект CoworkingDTO является экземпляром ConferenceRoomDTO, используется toConferenceRoom.
     * Если объект CoworkingDTO является экземпляром WorkplaceDTO, используется toWorkplace.
     *
     * @param coworkingDTO объект CoworkingDTO для преобразования
     * @return соответствующий Coworking
     * @throws CoworkingMappingException если тип CoworkingDTO не поддерживается
     */
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