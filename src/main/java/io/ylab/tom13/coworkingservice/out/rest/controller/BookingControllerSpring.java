package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.rest.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер бронирования.
 * Обрабатывает запросы, связанные с бронированием в системе.
 */
@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@Tag(name = "Контроллер бронирования", description = "Обрабатывает запросы, связанные с бронированием в системе.")
public class BookingControllerSpring {
    private final BookingService bookingService;

    /**
     * Создание нового бронирования.
     *
     * @param bookingDTO DTO с данными для бронирования.
     * @return ResponseEntity с информацией о созданном бронировании или сообщением об ошибке.
     */
    @Operation(summary = "Создание бронирования", description = "Создает новое бронирование и возвращает его данные.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Бронирование создано"),
            @ApiResponse(responseCode = "409", description = "Конфликт бронирования"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@Parameter(description = "Данные для создания бронирования", required = true) @RequestBody BookingDTO bookingDTO) throws BookingConflictException, RepositoryException {
        BookingDTO booking = bookingService.createBooking(bookingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    /**
     * Отмена существующего бронирования.
     *
     * @param bookingId ID бронирования для отмены.
     * @return ResponseEntity с сообщением об успешной отмене или ошибке.
     */
    @Operation(summary = "Отмена бронирования", description = "Отменяет бронирование по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Бронирование отменено"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@Parameter(description = "ID бронирования для отмены", required = true) @RequestParam(name = "bookingId") long bookingId) throws BookingNotFoundException, RepositoryException {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Получение бронирований пользователя", description = "Возвращает список бронирований пользователя по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирования не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/user")
    public ResponseEntity<?> getBookingsByUser(@Parameter(description = "ID пользователя для получения бронирований", required = true) @RequestParam(name = "userId") long userId) throws BookingNotFoundException, RepositoryException {
        List<BookingDTO> bookingsByUser = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookingsByUser);
    }

    /**
     * Получение бронирований по пользователю и дате.
     *
     * @param userId ID пользователя для получения бронирований.
     * @param date   Дата для фильтрации бронирований по дате.
     * @return ResponseEntity с коллекцией бронирований пользователя.
     */
    @Operation(summary = "Получение бронирований пользователя по дате", description = "Возвращает список бронирований пользователя по ID и дате.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирования не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/user/date")
    public ResponseEntity<?> getBookingsByUserAndDate(@Parameter(description = "ID пользователя для получения бронирований", required = true)
                                                      @RequestParam(name = "userId") long userId,
                                                      @Parameter(description = "Дата для получения бронирований", required = true)
                                                      @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws BookingNotFoundException, RepositoryException {
        List<BookingDTO> bookingsByUserAndDate = bookingService.getBookingsByUserAndDate(userId, date);
        return ResponseEntity.ok(bookingsByUserAndDate);
    }

    /**
     * Получение бронирований по пользователю и коворкингу.
     *
     * @param userId      ID пользователя для получения бронирований.
     * @param coworkingId ID коворкинга для фильтрации.
     * @return ResponseEntity с коллекцией бронирований пользователя.
     */
    @Operation(summary = "Получение бронирований пользователя по коворкингу", description = "Возвращает список бронирований пользователя по ID и ID коворкинга.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирования не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/user/coworking")
    public ResponseEntity<?> getBookingsByUserAndCoworking(@Parameter(description = "ID пользователя для получения бронирований", required = true)
                                                           @RequestParam(name = "userId") long userId,
                                                           @Parameter(description = "ID коворкинга для получения бронирований", required = true)
                                                           @RequestParam(name = "coworkingId") long coworkingId) {
        List<BookingDTO> bookingsByUserAndCoworking = bookingService.getBookingsByUserAndCoworking(userId, coworkingId);
        return ResponseEntity.ok(bookingsByUserAndCoworking);
    }

    /**
     * Получение свободных слотов для бронирования по коворкингу и дате.
     *
     * @param coworkingId ID коворкинга для фильтрации свободных слотов.
     * @param date        Дата для фильтрации свободных слотов.
     * @return ResponseEntity с коллекцией доступных временных слотов.
     */
    @Operation(summary = "Получение доступных временных слотов", description = "Возвращает список доступных временных слотов для указанного коворкинга и даты.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/availableslots")
    public ResponseEntity<?> getAvailableSlots(@Parameter(description = "ID коворкинга для получения временных слотов", required = true)
                                               @RequestParam(name = "coworkingId") long coworkingId,
                                               @Parameter(description = "Дата для получения временных слотов", required = true)
                                               @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TimeSlot> availableSlots = bookingService.getAvailableSlots(coworkingId, date);
        return ResponseEntity.ok(availableSlots);
    }

    /**
     * Получение бронирования по ID.
     *
     * @param bookingId ID искомого бронирования.
     * @return ResponseEntity с найденным бронированием или сообщением об ошибке.
     */
    @Operation(summary = "Получение бронирования по ID", description = "Возвращает данные бронирования по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<?> getBookingById(@Parameter(description = "ID бронирования для получения", required = true) @RequestParam(name = "bookingId") long bookingId) {
        BookingDTO bookingById = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(bookingById);
    }

    /**
     * Обновление информации о бронировании.
     *
     * @param booking данные бронирования для внесения изменений.
     * @return ResponseEntity с обновленными данными бронирования или сообщением об ошибке.
     */
    @Operation(summary = "Обновление бронирования", description = "Обновляет данные существующего бронирования.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "409", description = "Конфликт бронирования"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/update")
    public ResponseEntity<?> updateBooking(@Parameter(description = "Данные для обновления бронирования", required = true) @RequestBody BookingDTO booking) {
            BookingDTO updatedBooking = bookingService.updateBooking(booking);
            return ResponseEntity.ok(updatedBooking);
    }
}