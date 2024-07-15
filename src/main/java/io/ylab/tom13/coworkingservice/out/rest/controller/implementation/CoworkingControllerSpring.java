package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * Реализация интерфейса {@link CoworkingController}.
 * Обрабатывает запросы, связанные с управлением коворкингами.
 */
@RestController
@RequestMapping("/coworking")
@Tag(name = "Контроллер коворкингов", description = "Обрабатывает запросы, связанные с управлением коворкингами.")
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
    @Operation(summary = "Получить все коворкинги", description = "Возвращает список всех коворкингов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
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
    @Operation(summary = "Получить все доступные коворкинги", description = "Возвращает список всех доступных коворкингов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
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
    @Operation(summary = "Создать коворкинг", description = "Создает новый коворкинг и возвращает его данные.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Коворкинг создан"),
            @ApiResponse(responseCode = "409", description = "Конфликт при создании коворкинга"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createCoworking(@Parameter(description = "Данные для создания коворкинга", required = true) @RequestBody CoworkingDTO coworkingDTO) {
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
    @Operation(summary = "Обновить коворкинг", description = "Обновляет данные существующего коворкинга.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Коворкинг обновлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка обновления коворкинга"),
            @ApiResponse(responseCode = "404", description = "Коворкинг не найден"),
            @ApiResponse(responseCode = "409", description = "Конфликт при обновлении коворкинга"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/update")
    public ResponseEntity<?> updateCoworking(@Parameter(description = "Данные для обновления коворкинга", required = true) @RequestBody CoworkingDTO coworkingDTO) {
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
    @Operation(summary = "Удалить коворкинг", description = "Удаляет коворкинг по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Коворкинг удален"),
            @ApiResponse(responseCode = "404", description = "Коворкинг не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCoworking(@Parameter(description = "ID коворкинга для удаления", required = true) @RequestParam(name = "coworkingId") long coworkingId) {
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
