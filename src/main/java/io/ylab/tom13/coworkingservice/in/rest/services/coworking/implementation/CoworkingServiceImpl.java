package io.ylab.tom13.coworkingservice.in.rest.services.coworking.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.in.rest.repositories.BookingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.BookingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.CoworkingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.coworking.CoworkingService;

import java.util.Map;

public class CoworkingServiceImpl implements CoworkingService {

    private final CoworkingRepository coworkingRepository;

    private final BookingRepository bookingRepository;

    public CoworkingServiceImpl() {
        coworkingRepository = CoworkingRepositoryCollection.getInstance();
        bookingRepository = BookingRepositoryCollection.getInstance();
    }

    @Override
    public Map<String, CoworkingDTO> getAllCoworking() {
        return coworkingRepository.getAllCoworking();
    }

    @Override
    public Map<String, CoworkingDTO> getAllAvailableCoworkings() {
        return coworkingRepository.getAllAvailableCoworkings();
    }

    @Override
    public CoworkingDTO createCoworking(CoworkingDTO coworkingDTO) throws CoworkingConflictException {
        return coworkingRepository.createCoworking(coworkingDTO);
    }

    @Override
    public CoworkingDTO updateCoworking(CoworkingDTO coworkingDTO) throws CoworkingUpdatingExceptions {
        try {
            return coworkingRepository.updateCoworking(coworkingDTO);
        } catch (CoworkingNotFoundException | CoworkingConflictException e) {
            throw new CoworkingUpdatingExceptions(e.getMessage());
        }
    }

    @Override
    public void deleteBooking(long coworkingId) throws CoworkingNotFoundException {
        coworkingRepository.deleteCoworking(coworkingId);
        bookingRepository.deleteAllCoworkingBookings(coworkingId);
    }
}
