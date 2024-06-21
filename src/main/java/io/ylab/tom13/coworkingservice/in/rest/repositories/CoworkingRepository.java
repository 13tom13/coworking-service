package io.ylab.tom13.coworkingservice.in.rest.repositories;

import io.ylab.tom13.coworkingservice.in.entity.dto.space.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.WorkplaceDTO;

import java.util.Map;

public interface CoworkingRepository {

    WorkplaceDTO addWorkplace(WorkplaceDTO workplaceDTO);

    ConferenceRoomDTO addConferenceRoom(ConferenceRoomDTO conferenceRoomDTO);

    void deleteWorkplace(String workplaceId);

    void deleteConferenceRoom(String conferenceRoomId);

    Map<String, CoworkingDTO> getAllCoworking();
}
