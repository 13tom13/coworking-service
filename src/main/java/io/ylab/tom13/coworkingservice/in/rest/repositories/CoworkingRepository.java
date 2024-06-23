package io.ylab.tom13.coworkingservice.in.rest.repositories;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;

import java.util.Map;

public interface CoworkingRepository {

    Map<String, CoworkingDTO> getAllCoworking();

    Map<String, CoworkingDTO> getAllAvailableCoworkings();

    CoworkingDTO createCoworking(CoworkingDTO coworkingDTO) throws CoworkingConflictException;

    void deleteCoworking(long coworkingId) throws CoworkingNotFoundException;

    CoworkingDTO updateCoworking(CoworkingDTO coworkingDTO) throws CoworkingNotFoundException, CoworkingConflictException;
}
