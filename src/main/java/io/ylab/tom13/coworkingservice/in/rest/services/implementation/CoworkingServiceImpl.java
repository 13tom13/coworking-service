package io.ylab.tom13.coworkingservice.in.rest.services.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.BookingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.CoworkingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.CoworkingService;
import io.ylab.tom13.coworkingservice.in.utils.SecurityController;
import io.ylab.tom13.coworkingservice.in.utils.mapper.ConferenceRoomMapper;
import io.ylab.tom13.coworkingservice.in.utils.mapper.WorkplaceMapper;

import java.util.*;


public class CoworkingServiceImpl extends SecurityController implements CoworkingService {

    private final CoworkingRepository coworkingRepository;

    private final BookingRepository bookingRepository;

    private final WorkplaceMapper workplaceMapper = WorkplaceMapper.INSTANCE;

    private final ConferenceRoomMapper conferenceRoomMapper = ConferenceRoomMapper.INSTANCE;

    public CoworkingServiceImpl() {
        coworkingRepository = CoworkingRepositoryCollection.getInstance();
        bookingRepository = BookingRepositoryCollection.getInstance();
    }

    @Override
    public Map<String, CoworkingDTO> getAllCoworking() {
        Collection<Coworking> allCoworking = coworkingRepository.getAllCoworking();
        return getCoworkingByNameHashMap(allCoworking);
    }

    @Override
    public Map<String, CoworkingDTO> getAllAvailableCoworkings() {
        Collection<Coworking> allCoworking = coworkingRepository.getAllCoworking();
        List<Coworking> availableCoworking = allCoworking.stream()
                .filter(Coworking::isAvailable)
                .toList();
        return getCoworkingByNameHashMap(availableCoworking);
    }

    @Override
    public CoworkingDTO createCoworking(CoworkingDTO coworkingDTO) throws CoworkingConflictException, RepositoryException {
        Coworking coworking = convertToEntity(coworkingDTO);
        Optional<Coworking> coworkingFromRepository = coworkingRepository.createCoworking(coworking);
        if (coworkingFromRepository.isPresent()) {
            return convertToDTO(coworkingFromRepository.get());
        } else {
            throw new RepositoryException("Не удалось создать коворкинг");
        }
    }


    @Override
    public CoworkingDTO updateCoworking(CoworkingDTO coworkingDTO) throws CoworkingUpdatingExceptions, CoworkingNotFoundException, CoworkingConflictException {
        Coworking coworking = convertToEntity(coworkingDTO);
        Optional<Coworking> coworkingFromRepository = coworkingRepository.updateCoworking(coworking);
        if (coworkingFromRepository.isPresent()) {
            return convertToDTO(coworkingFromRepository.get());
        } else {
            throw new CoworkingUpdatingExceptions("Не удалось обновить коворкинг");

        }
    }

    @Override
    public void deleteBooking(long coworkingId) throws CoworkingNotFoundException {
        coworkingRepository.deleteCoworking(coworkingId);
        bookingRepository.deleteAllCoworkingBookings(coworkingId);
    }

    private Map<String, CoworkingDTO> getCoworkingByNameHashMap(Collection<Coworking> allCoworking) {
        Map<String, CoworkingDTO> allCoworkingDTO = new HashMap<>();
        for (Coworking coworking : allCoworking) {
            try {
                allCoworkingDTO.put(coworking.getName(), convertToDTO(coworking));
            } catch (CoworkingConflictException ignored) {

            }
        }
        return allCoworkingDTO;
    }


    private Coworking convertToEntity(CoworkingDTO coworkingDTO) throws CoworkingConflictException {
        if (coworkingDTO instanceof WorkplaceDTO) {
            return workplaceMapper.toWorkplace((WorkplaceDTO) coworkingDTO);
        } else if (coworkingDTO instanceof ConferenceRoomDTO) {
            return conferenceRoomMapper.toConferenceRoom((ConferenceRoomDTO) coworkingDTO);
        } else {
            throw new CoworkingConflictException("Не известный тип коворкинга");
        }
    }

    private CoworkingDTO convertToDTO(Coworking coworking) throws CoworkingConflictException {
        if (coworking instanceof Workplace) {
            return workplaceMapper.toWorkplaceDTO((Workplace) coworking);
        } else if (coworking instanceof ConferenceRoom) {
            return conferenceRoomMapper.toConferenceRoomDTO((ConferenceRoom) coworking);
        } else {
            throw new CoworkingConflictException("Не известный тип коворкинга");
        }
    }

}
