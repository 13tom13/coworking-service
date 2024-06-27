package io.ylab.tom13.coworkingservice.in.rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;

import java.util.Map;

public interface CoworkingService {

    Map<String, CoworkingDTO> getAllCoworking();

    Map<String, CoworkingDTO> getAllAvailableCoworkings();

    CoworkingDTO createCoworking(CoworkingDTO coworkingDTO) throws CoworkingConflictException, RepositoryException;


    CoworkingDTO updateCoworking(CoworkingDTO coworkingDTO) throws CoworkingUpdatingExceptions, CoworkingNotFoundException, CoworkingConflictException;

    void deleteCoworking(long coworkingId) throws CoworkingNotFoundException;
}
