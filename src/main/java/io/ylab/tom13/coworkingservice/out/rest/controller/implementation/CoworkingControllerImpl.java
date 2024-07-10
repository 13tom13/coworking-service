package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.CoworkingController;
import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.CoworkingServiceImpl;
import io.ylab.tom13.coworkingservice.out.utils.security.SecurityHTTPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * Реализация интерфейса {@link CoworkingControllerImpl}.
 * Обрабатывает запросы, связанные с управлением коворкингами.
 */
@Controller
public class CoworkingControllerImpl implements CoworkingController {

    private final CoworkingService coworkingService;

    /**
     * Конструктор для инициализации контроллера коворкингов.
     */
    @Autowired
    public CoworkingControllerImpl(CoworkingService coworkingService) {
        this.coworkingService = coworkingService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<Map<String, CoworkingDTO>> getAllCoworking() {
//        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
//            return ResponseDTO.failure(new NoAccessException().getMessage());
//        }
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
//        if (!hasAuthenticated()) {
//            return ResponseDTO.failure(new UnauthorizedException().getMessage());
//        }
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
//        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
//            return ResponseDTO.failure(new NoAccessException().getMessage());
//        }
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
//        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
//            return ResponseDTO.failure(new NoAccessException().getMessage());
//        }
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
//        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
//            return ResponseDTO.failure(new NoAccessException().getMessage());
//        }
        try {
            coworkingService.deleteCoworking(coworkingId);
            return ResponseDTO.success(null);
        } catch (CoworkingNotFoundException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}
