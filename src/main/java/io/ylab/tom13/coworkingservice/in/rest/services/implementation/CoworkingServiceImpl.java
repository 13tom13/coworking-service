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


/**
 * Реализация интерфейса {@link CoworkingService}.
 * Сервиса управления коворкингами.
 */
public class CoworkingServiceImpl extends SecurityController implements CoworkingService {

    private final CoworkingRepository coworkingRepository;
    private final BookingRepository bookingRepository;
    private final WorkplaceMapper workplaceMapper = WorkplaceMapper.INSTANCE;
    private final ConferenceRoomMapper conferenceRoomMapper = ConferenceRoomMapper.INSTANCE;

    /**
     * Конструктор по умолчанию.
     * Инициализирует репозитории для работы с данными коворкингов и бронирований.
     */
    public CoworkingServiceImpl() {
        coworkingRepository = CoworkingRepositoryCollection.getInstance();
        bookingRepository = BookingRepositoryCollection.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, CoworkingDTO> getAllCoworking() {
        Collection<Coworking> allCoworking = coworkingRepository.getAllCoworking();
        return getCoworkingByNameHashMap(allCoworking);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, CoworkingDTO> getAllAvailableCoworkings() {
        Collection<Coworking> allCoworking = coworkingRepository.getAllCoworking();
        List<Coworking> availableCoworking = allCoworking.stream()
                .filter(Coworking::isAvailable)
                .toList();
        return getCoworkingByNameHashMap(availableCoworking);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCoworking(long coworkingId) throws CoworkingNotFoundException {
        coworkingRepository.deleteCoworking(coworkingId);
        bookingRepository.deleteAllCoworkingBookings(coworkingId);
    }

    /**
     * Преобразует коллекцию коворкингов в карту по имени коворкинга.
     *
     * @param allCoworking коллекция коворкингов для преобразования.
     * @return Map с информацией о коворкингах, где ключ - имя коворкинга, значение - DTO коворкинга.
     */
    private Map<String, CoworkingDTO> getCoworkingByNameHashMap(Collection<Coworking> allCoworking) {
        Map<String, CoworkingDTO> allCoworkingDTO = new HashMap<>();
        for (Coworking coworking : allCoworking) {
            try {
                allCoworkingDTO.put(coworking.getName(), convertToDTO(coworking));
            } catch (CoworkingConflictException ignored) {
                // Handle mapping exception if needed
            }
        }
        return allCoworkingDTO;
    }

    /**
     * Преобразует DTO коворкинга в соответствующую сущность.
     *
     * @param coworkingDTO DTO коворкинга для преобразования.
     * @return соответствующая сущность коворкинга.
     * @throws CoworkingConflictException если возникает конфликт при преобразовании.
     */
    private Coworking convertToEntity(CoworkingDTO coworkingDTO) throws CoworkingConflictException {
        if (coworkingDTO instanceof WorkplaceDTO) {
            return workplaceMapper.toWorkplace((WorkplaceDTO) coworkingDTO);
        } else if (coworkingDTO instanceof ConferenceRoomDTO) {
            return conferenceRoomMapper.toConferenceRoom((ConferenceRoomDTO) coworkingDTO);
        } else {
            throw new CoworkingConflictException("Не известный тип коворкинга");
        }
    }

    /**
     * Преобразует сущность коворкинга в соответствующий DTO.
     *
     * @param coworking сущность коворкинга для преобразования.
     * @return соответствующий DTO коворкинга.
     * @throws CoworkingConflictException если возникает конфликт при преобразовании.
     */
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
