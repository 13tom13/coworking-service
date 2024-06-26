package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    private final Map<Long, Coworking> coworkingById = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);


    @Override
    public Collection<Coworking> getAllCoworking() {
        return coworkingById.values();
    }

    @Override
    public Optional<Coworking> getCoworkingById(long coworkingId) {
        return Optional.ofNullable(coworkingById.get(coworkingId));
    }


    @Override
    public Optional<Coworking> createCoworking(Coworking coworking) throws CoworkingConflictException, RepositoryException {
        if (!isCoworkingNameUnique(coworking.getName())) {
            throw new CoworkingConflictException("Имя коворкинга уже занято");
        }
        if (coworking instanceof Workplace) {
            coworking = new Workplace(idCounter.incrementAndGet(), coworking.getName(), coworking.getDescription(), coworking.isAvailable(), ((Workplace) coworking).getType());
        } else if (coworking instanceof ConferenceRoom) {
            coworking = new ConferenceRoom(idCounter.incrementAndGet(), coworking.getName(), coworking.getDescription(), coworking.isAvailable(), ((ConferenceRoom) coworking).getCapacity());
        } else {
            throw new RepositoryException("Неизвестный тип коворкинга");
        }
        coworkingById.put(coworking.getId(), coworking);
        return Optional.ofNullable(coworkingById.get(coworking.getId()));
    }

    @Override
    public void deleteCoworking(long coworkingId) throws CoworkingNotFoundException {
        if (coworkingById.containsKey(coworkingId)) {
            coworkingById.remove(coworkingId);
        } else {
            throw new CoworkingNotFoundException("Коворкинг с указанным ID не найден");
        }
    }

    @Override
    public Optional<Coworking> updateCoworking(Coworking coworking) throws CoworkingConflictException, CoworkingNotFoundException {
        Coworking existingWorkplace = coworkingById.get(coworking.getId());
        if (existingWorkplace == null) {
            throw new CoworkingNotFoundException("Коворкинг с указанным ID не найдено");
        }
        if (!existingWorkplace.getName().equals(coworking.getName()) &&
            !isCoworkingNameUnique(coworking.getName())) {
            throw new CoworkingConflictException("Имя рабочего места уже занято");
        }
        coworkingById.put(coworking.getId(), coworking);
        return Optional.ofNullable(coworkingById.get(coworking.getId()));
    }

    private boolean isCoworkingNameUnique(String name) {
        return coworkingById.values().stream().noneMatch(coworking -> coworking.getName().equals(name));
    }


}
