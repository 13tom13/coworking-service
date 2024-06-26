package io.ylab.tom13.coworkingservice.in.rest.controller.coworking.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.controller.coworking.CoworkingController;
import io.ylab.tom13.coworkingservice.in.rest.services.coworking.CoworkingService;
import io.ylab.tom13.coworkingservice.in.rest.services.coworking.implementation.CoworkingServiceImpl;

import java.util.Map;

public class CoworkingControllerImpl implements CoworkingController {

    private final CoworkingService coworkingService;

    public CoworkingControllerImpl() {
        coworkingService = new CoworkingServiceImpl();
    }

    @Override
    public ResponseDTO<Map<String, CoworkingDTO>> getAllCoworking() {
        Map<String, CoworkingDTO> allCoworking = coworkingService.getAllCoworking();
        return ResponseDTO.success(allCoworking);
    }

    @Override
    public ResponseDTO<Map<String, CoworkingDTO>> getAllAvailableCoworkings() {
        Map<String, CoworkingDTO> allCoworking = coworkingService.getAllAvailableCoworkings();
        return ResponseDTO.success(allCoworking);
    }

    @Override
    public ResponseDTO<CoworkingDTO> createCoworking(CoworkingDTO coworkingDTO) {
        try {
            CoworkingDTO coworking = coworkingService.createCoworking(coworkingDTO);
            return ResponseDTO.success(coworking);
        } catch (CoworkingConflictException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<CoworkingDTO> updateCoworking(CoworkingDTO coworkingDTO) {
        try {
            CoworkingDTO coworking = coworkingService.updateCoworking(coworkingDTO);
            return ResponseDTO.success(coworking);
        } catch (CoworkingUpdatingExceptions | CoworkingNotFoundException | CoworkingConflictException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<Void> deleteBooking(long coworkingId) {
        try {
            coworkingService.deleteBooking(coworkingId);
            return ResponseDTO.success(null);
        } catch (CoworkingNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}
