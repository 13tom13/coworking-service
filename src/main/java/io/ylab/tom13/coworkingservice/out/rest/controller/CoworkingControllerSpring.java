package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для работы с коворкингами.
 * Обрабатывает запросы, связанные с управлением коворкингами.
 */
@RestController
@RequestMapping("/coworking")
@RequiredArgsConstructor
@Tag(name = "Контроллер коворкингов", description = "Обрабатывает запросы, связанные с управлением коворкингами.")
public class CoworkingControllerSpring {

    private final CoworkingService coworkingService;

    /**
     * Получение всех коворкингов.
     *
     * @return ResponseEntity с картой всех коворкингов, где ключ - имя коворкинга, значение - DTO коворкинга.
     */
    @Operation(summary = "Получить все коворкинги", description = "Возвращает список всех коворкингов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/all")
    public ResponseEntity<?> getAllCoworking() {
        Map<String, CoworkingDTO> allCoworking = coworkingService.getAllCoworking();
        return ResponseEntity.ok(allCoworking);
    }


    /**
     * Получение всех доступных коворкингов.
     *
     * @return ResponseEntity с картой всех доступных коворкингов, где ключ - имя коворкинга, значение - DTO коворкинга.
     */
    @Operation(summary = "Получить все доступные коворкинги", description = "Возвращает список всех доступных коворкингов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/available")
    public ResponseEntity<?> getAllAvailableCoworkings() {
        Map<String, CoworkingDTO> allCoworking = coworkingService.getAllAvailableCoworkings();
        return ResponseEntity.ok(allCoworking);
    }

    /**
     * Создание нового коворкинга.
     *
     * @param coworkingDTO DTO с данными для создания коворкинга.
     * @return ResponseEntity с информацией о созданном коворкинге или сообщением об ошибке.
     */
    @Operation(summary = "Создать коворкинг", description = "Создает новый коворкинг и возвращает его данные.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Коворкинг создан"),
            @ApiResponse(responseCode = "409", description = "Конфликт при создании коворкинга"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createCoworking(@Parameter(description = "Данные для создания коворкинга", required = true) @RequestBody CoworkingDTO coworkingDTO) {
        CoworkingDTO coworking = coworkingService.createCoworking(coworkingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(coworking);
    }

    /**
     * Обновление информации о коворкинге.
     *
     * @param coworkingDTO DTO с данными для обновления коворкинга.
     * @return ResponseEntity с обновленными данными коворкинга или сообщением об ошибке.
     */
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
        CoworkingDTO coworking = coworkingService.updateCoworking(coworkingDTO);
        return ResponseEntity.ok(coworking);
    }

    /**
     * Удаление коворкинга по ID.
     *
     * @param coworkingId ID коворкинга для удаления.
     * @return ResponseEntity с сообщением об успешном удалении или ошибке.
     */
    @Operation(summary = "Удалить коворкинг", description = "Удаляет коворкинг по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Коворкинг удален"),
            @ApiResponse(responseCode = "404", description = "Коворкинг не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCoworking(@Parameter(description = "ID коворкинга для удаления", required = true) @RequestParam(name = "coworkingId") long coworkingId) {
        String responseSuccess = "Коворкинг успешно удален";
        coworkingService.deleteCoworking(coworkingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseSuccess);
    }
}
