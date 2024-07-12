package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.rest.controller.CoworkingController;
import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Реализация интерфейса {@link CoworkingControllerSpring}.
 * Обрабатывает запросы, связанные с управлением коворкингами.
 */
@RestController
@RequestMapping("/coworking")
public class CoworkingControllerSpring implements CoworkingController {

    private final CoworkingService coworkingService;

    /**
     * Конструктор для инициализации контроллера коворкингов.
     */
    @Autowired
    public CoworkingControllerSpring(CoworkingService coworkingService) {
        this.coworkingService = coworkingService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetMapping("/all")
    public ResponseEntity<?> getAllCoworking() {
        try {
            Map<String, CoworkingDTO> allCoworking = coworkingService.getAllCoworking();
            return ResponseEntity.ok(allCoworking);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetMapping("/available")
    public ResponseEntity<?> getAllAvailableCoworkings() {
        try {
            Map<String, CoworkingDTO> allCoworking = coworkingService.getAllAvailableCoworkings();
            return ResponseEntity.ok(allCoworking);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping("/create")
    public ResponseEntity<?> createCoworking(@RequestBody CoworkingDTO coworkingDTO) {
        try {
            CoworkingDTO coworking = coworkingService.createCoworking(coworkingDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(coworking);
        } catch (CoworkingConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PatchMapping("/update")
    public ResponseEntity<?> updateCoworking(@RequestBody CoworkingDTO coworkingDTO) {
        try {
            CoworkingDTO coworking = coworkingService.updateCoworking(coworkingDTO);
            return ResponseEntity.ok(coworking);
        } catch (CoworkingUpdatingExceptions e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (CoworkingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CoworkingConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCoworking(@RequestParam(name = "coworkingId")long coworkingId) {
        try {
            String responseSuccess = "Коворкинг успешно удален";
            coworkingService.deleteCoworking(coworkingId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseSuccess);
        } catch (CoworkingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
