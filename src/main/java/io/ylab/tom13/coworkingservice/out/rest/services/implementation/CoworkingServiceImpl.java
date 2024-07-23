package io.ylab.tom13.coworkingservice.out.rest.services.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.out.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import io.ylab.tom13.coworkingservice.out.utils.mapper.CoworkingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Реализация интерфейса {@link CoworkingService}.
 * Сервиса управления коворкингами.
 */
@Service
@RequiredArgsConstructor
public class CoworkingServiceImpl implements CoworkingService {

    private final CoworkingRepository coworkingRepository;
    private final BookingRepository bookingRepository;

    private final CoworkingMapper coworkingMapper = CoworkingMapper.INSTANCE;

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
        List<Coworking> availableCoworking = allCoworking.stream().filter(Coworking::isAvailable).toList();
        return getCoworkingByNameHashMap(availableCoworking);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoworkingDTO createCoworking(CoworkingDTO coworkingDTO) {
        Coworking coworking = coworkingMapper.toCoworking(coworkingDTO);
        Optional<Coworking> coworkingFromRepository = coworkingRepository.createCoworking(coworking);
        if (coworkingFromRepository.isPresent()) {
            return coworkingMapper.toCoworkingDTO(coworkingFromRepository.get());
        } else {
            throw new RepositoryException("Не удалось создать коворкинг");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoworkingDTO updateCoworking(CoworkingDTO coworkingDTO) {
        Coworking coworking = coworkingMapper.toCoworking(coworkingDTO);
        Optional<Coworking> coworkingFromRepository = coworkingRepository.updateCoworking(coworking);
        if (coworkingFromRepository.isPresent()) {
            return coworkingMapper.toCoworkingDTO(coworkingFromRepository.get());
        } else {
            throw new CoworkingUpdatingExceptions("Не удалось обновить коворкинг");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCoworking(long coworkingId) {
        bookingRepository.deleteAllCoworkingBookings(coworkingId);
        coworkingRepository.deleteCoworking(coworkingId);
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
            allCoworkingDTO.put(coworking.getName(), coworkingMapper.toCoworkingDTO(coworking));
        }
        return allCoworkingDTO;
    }

}
