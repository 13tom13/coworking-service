package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.in.utils.mapper.ConferenceRoomMapper;
import io.ylab.tom13.coworkingservice.in.utils.mapper.WorkplaceMapper;

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

    private final WorkplaceMapper workplaceMapper = WorkplaceMapper.INSTANCE;
    private final ConferenceRoomMapper conferenceRoomMapper = ConferenceRoomMapper.INSTANCE;

    @Override
    public Map<String, CoworkingDTO> getAllCoworking() {
        Map<String, CoworkingDTO> AllCoworking = new HashMap<>();

        for (Workplace workplace : workplacesById.values()) {
            AllCoworking.put(workplace.getName(), workplaceMapper.toWorkplaceDTO(workplace));
        }

        for (ConferenceRoom conferenceRoom : conferenceRoomsById.values()) {
            AllCoworking.put(conferenceRoom.getName(), conferenceRoomMapper.toConferenceRoomDTO(conferenceRoom));
        }
        return AllCoworking;
    }

    @Override
    public Map<String, CoworkingDTO> getAllAvailableCoworkings() {
        Map<String, CoworkingDTO> allCoworkings = getAllCoworking();
        Map<String, CoworkingDTO> availableCoworkings = new HashMap<>();

        for (Map.Entry<String, CoworkingDTO> entry : allCoworkings.entrySet()) {
            if (entry.getValue().isAvailable()) {
                availableCoworkings.put(entry.getKey(), entry.getValue());
            }
        }
        return availableCoworkings;
    }

    @Override
    public CoworkingDTO createCoworking(CoworkingDTO coworkingDTO) throws CoworkingConflictException {
        if (!isCoworkingNameUnique(coworkingDTO.getName())) {
            throw new CoworkingConflictException("Имя коворкинга уже занято");
        }

        if (coworkingDTO instanceof WorkplaceDTO workplaceDTO) {
            Workplace workplace = workplaceMapper.toWorkplace(workplaceDTO, idCounter.incrementAndGet());
            workplacesById.put(workplace.getId(), workplace);
            return workplaceMapper.toWorkplaceDTO(workplace);
        } else if (coworkingDTO instanceof ConferenceRoomDTO conferenceRoomDTO) {
            ConferenceRoom conferenceRoom = conferenceRoomMapper.toConferenceRoom(conferenceRoomDTO, idCounter.incrementAndGet());
            conferenceRoomsById.put(conferenceRoom.getId(), conferenceRoom);
            return conferenceRoomMapper.toConferenceRoomDTO(conferenceRoom);
        } else {
            throw new CoworkingConflictException("Не известный тип коворкинга");
        }
    }

    @Override
    public void deleteCoworking(long coworkingId) throws CoworkingNotFoundException {
        if (workplacesById.containsKey(coworkingId)) {
            workplacesById.remove(coworkingId);
        } else if (conferenceRoomsById.containsKey(coworkingId)) {
            conferenceRoomsById.remove(coworkingId);
        } else {
            throw new CoworkingNotFoundException("Коворкинг с указанным ID не найден");
        }
    }

    @Override
    public CoworkingDTO updateCoworking(CoworkingDTO coworkingDTO) throws CoworkingConflictException, CoworkingNotFoundException {
        if (coworkingDTO instanceof WorkplaceDTO workplaceDTO) {
            Workplace existingWorkplace = workplacesById.get(workplaceDTO.getId());
            if (existingWorkplace == null) {
                throw new CoworkingNotFoundException("Рабочее место с указанным ID не найдено");
            }
            if (!existingWorkplace.getName().equals(workplaceDTO.getName()) &&
                !isCoworkingNameUnique(workplaceDTO.getName())) {
                throw new CoworkingConflictException("Имя рабочего места уже занято");
            }
            Workplace workplace = workplaceMapper.toWorkplace(workplaceDTO);
            workplacesById.put(workplace.getId(), workplace);
            return workplaceMapper.toWorkplaceDTO(workplace);

        } else if (coworkingDTO instanceof ConferenceRoomDTO conferenceRoomDTO) {
            ConferenceRoom existingConferenceRoom = conferenceRoomsById.get(conferenceRoomDTO.getId());
            if (existingConferenceRoom == null) {
                throw new CoworkingConflictException("Конференц-зал с указанным ID не найден");
            }

            if (!existingConferenceRoom.getName().equals(conferenceRoomDTO.getName()) &&
                !isCoworkingNameUnique(conferenceRoomDTO.getName())) {
                throw new CoworkingConflictException("Имя конференц-зала уже занято");
            }
            ConferenceRoom conferenceRoom = conferenceRoomMapper.toConferenceRoom(conferenceRoomDTO);
            conferenceRoomsById.put(conferenceRoom.getId(), conferenceRoom);
            return conferenceRoomMapper.toConferenceRoomDTO(conferenceRoom);
        } else {
            throw new CoworkingConflictException("Неизвестный тип коворкинга");
        }
    }

    private boolean isCoworkingNameUnique(String name) {
        return workplacesById.values().stream().noneMatch(workplace -> workplace.getName().equals(name)) &&
               conferenceRoomsById.values().stream().noneMatch(conferenceRoom -> conferenceRoom.getName().equals(name));
    }


}
