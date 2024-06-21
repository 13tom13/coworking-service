package io.ylab.tom13.coworkingservice.in.rest.services.booking.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.rest.repositories.CoworkingRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.CoworkingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.booking.CoworkingService;

import java.util.Map;

public class CoworkingServiceImpl implements CoworkingService {

    private final CoworkingRepository coworkingRepository;

    public CoworkingServiceImpl() {
        coworkingRepository = CoworkingRepositoryCollection.getInstance();
    }

    @Override
    public  Map<String, CoworkingDTO> getAllCoworking() {
        return coworkingRepository.getAllCoworking();
    }
}
