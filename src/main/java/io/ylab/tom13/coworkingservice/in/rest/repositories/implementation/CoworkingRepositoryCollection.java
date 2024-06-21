package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.space.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.space.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.space.Workplace;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class CoworkingRepositoryCollection implements CoworkingRepository {

    private static final CoworkingRepositoryCollection INSTANCE = new CoworkingRepositoryCollection();

    private CoworkingRepositoryCollection() {
    }

    /**
     * Получение единственного экземпляра класса репозитория.
     *
     * @return экземпляр CoworkingRepositoryCollection
     */
    public static CoworkingRepositoryCollection getInstance() {
        return INSTANCE;
    }

    private final Map<Long, Workplace> workplacesById  = new HashMap<>();
    private final Map<Long, ConferenceRoom> conferenceRoomsById   = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public WorkplaceDTO addWorkplace(WorkplaceDTO workplaceDTO) {
        Workplace workplace = new Workplace(idCounter.incrementAndGet(),
                workplaceDTO.getName(), workplaceDTO.getDescription(),
                workplaceDTO.isAvailable(), workplaceDTO.getType());
        workplacesById.put(workplace.getId(), workplace);
        return new WorkplaceDTO(workplace.getId(),
                workplace.getName(), workplace.getDescription(),
                workplace.isAvailable(), workplace.getType());
    }

    @Override
    public ConferenceRoomDTO addConferenceRoom(ConferenceRoomDTO conferenceRoomDTO) {
        ConferenceRoom conferenceRoom  = new ConferenceRoom(idCounter.incrementAndGet(),
                conferenceRoomDTO.getName(), conferenceRoomDTO.getDescription(),
                conferenceRoomDTO.isAvailable(), conferenceRoomDTO.getCapacity());
        conferenceRoomsById.put(conferenceRoom.getId(), conferenceRoom);
        return new ConferenceRoomDTO(conferenceRoom.getId(),
                conferenceRoom.getName(), conferenceRoom.getDescription(),
                conferenceRoom.isAvailable(), conferenceRoom.getCapacity());
    }

    @Override
    public void deleteWorkplace(String workplaceId) {

    }

    @Override
    public void deleteConferenceRoom(String conferenceRoomId) {

    }

    @Override
    public Map<String, CoworkingDTO> getAllCoworking() {
        Map<String, CoworkingDTO> allSpaces = new HashMap<>();

        for (Workplace workplace : workplacesById.values()) {
            allSpaces.put(workplace.getName(), new WorkplaceDTO(workplace.getId(),
                    workplace.getName(), workplace.getDescription(),
                    workplace.isAvailable(), workplace.getType()));
        }

        for (ConferenceRoom conferenceRoom : conferenceRoomsById.values()) {
            allSpaces.put(conferenceRoom.getName(), new ConferenceRoomDTO(conferenceRoom.getId(),
                    conferenceRoom.getName(), conferenceRoom.getDescription(),
                    conferenceRoom.isAvailable(), conferenceRoom.getCapacity()));
        }

        return allSpaces;
    }
}
