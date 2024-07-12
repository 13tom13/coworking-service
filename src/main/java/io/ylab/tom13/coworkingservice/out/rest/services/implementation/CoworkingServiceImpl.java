package io.ylab.tom13.coworkingservice.out.rest.services.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.out.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import io.ylab.tom13.coworkingservice.out.util.mapper.CoworkingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Реализация интерфейса {@link CoworkingService}.
 * Сервиса управления коворкингами.
 */
@Service
public class CoworkingServiceImpl implements CoworkingService {

    private final CoworkingRepository coworkingRepository;
    private final BookingRepository bookingRepository;
    private final CoworkingMapper coworkingMapper = CoworkingMapper.INSTANCE;

    /**
     * Конструктор по умолчанию.
     * Инициализирует репозитории для работы с данными коворкингов и бронирований.
     */
    @Autowired
    public CoworkingServiceImpl(CoworkingRepository coworkingRepository, BookingRepository bookingRepository) {
        this.coworkingRepository = coworkingRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, CoworkingDTO> getAllCoworking() throws RepositoryException {
        Collection<Coworking> allCoworking = coworkingRepository.getAllCoworking();
        return getCoworkingByNameHashMap(allCoworking);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, CoworkingDTO> getAllAvailableCoworkings() throws RepositoryException {
        Collection<Coworking> allCoworking = coworkingRepository.getAllCoworking();
        List<Coworking> availableCoworking = allCoworking.stream().filter(Coworking::isAvailable).toList();
        return getCoworkingByNameHashMap(availableCoworking);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoworkingDTO createCoworking(CoworkingDTO coworkingDTO) throws CoworkingConflictException, RepositoryException {
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
    public CoworkingDTO updateCoworking(CoworkingDTO coworkingDTO) throws CoworkingUpdatingExceptions, CoworkingNotFoundException, CoworkingConflictException, RepositoryException {
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
    public void deleteCoworking(long coworkingId) throws CoworkingNotFoundException, RepositoryException {
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
