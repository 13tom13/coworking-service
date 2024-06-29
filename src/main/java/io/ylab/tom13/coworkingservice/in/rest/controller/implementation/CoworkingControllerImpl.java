package io.ylab.tom13.coworkingservice.in.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.controller.CoworkingController;
import io.ylab.tom13.coworkingservice.in.rest.services.CoworkingService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.CoworkingServiceImpl;
import io.ylab.tom13.coworkingservice.in.security.SecurityController;

import java.util.Map;

/**
 * Реализация интерфейса {@link CoworkingControllerImpl}.
 * Обрабатывает запросы, связанные с управлением коворкингами.
 */
public class CoworkingControllerImpl extends SecurityController implements CoworkingController {

    private final CoworkingService coworkingService;

    /**
     * Конструктор для инициализации контроллера коворкингов.
     */
    public CoworkingControllerImpl() {
        coworkingService = new CoworkingServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<Map<String, CoworkingDTO>> getAllCoworking() {
        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
            return ResponseDTO.failure(new NoAccessException().getMessage());
        }
        try {
            Map<String, CoworkingDTO> allCoworking = coworkingService.getAllCoworking();
            return ResponseDTO.success(allCoworking);
        } catch (RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<Map<String, CoworkingDTO>> getAllAvailableCoworkings() {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            Map<String, CoworkingDTO> allCoworking = coworkingService.getAllAvailableCoworkings();
            return ResponseDTO.success(allCoworking);
        } catch (RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<CoworkingDTO> createCoworking(CoworkingDTO coworkingDTO) {
        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
            return ResponseDTO.failure(new NoAccessException().getMessage());
        }
        try {
            CoworkingDTO coworking = coworkingService.createCoworking(coworkingDTO);
            return ResponseDTO.success(coworking);
        } catch (CoworkingConflictException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<CoworkingDTO> updateCoworking(CoworkingDTO coworkingDTO) {
        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
            return ResponseDTO.failure(new NoAccessException().getMessage());
        }
        try {
            CoworkingDTO coworking = coworkingService.updateCoworking(coworkingDTO);
            return ResponseDTO.success(coworking);
        } catch (CoworkingUpdatingExceptions | CoworkingNotFoundException | CoworkingConflictException |
                 RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<Void> deleteCoworking(long coworkingId) {
        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
            return ResponseDTO.failure(new NoAccessException().getMessage());
        }
        try {
            coworkingService.deleteCoworking(coworkingId);
            return ResponseDTO.success(null);
        } catch (CoworkingNotFoundException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}
