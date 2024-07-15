package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

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
import io.ylab.tom13.coworkingservice.out.rest.controller.BookingController;
import io.ylab.tom13.coworkingservice.out.rest.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Реализация интерфейса {@link BookingController}.
 * Обрабатывает запросы, связанные с бронированием в системе.
 */
@RestController
@RequestMapping("/booking")
@Tag(name = "Контроллер бронирования", description = "Обрабатывает запросы, связанные с бронированием в системе.")
public class BookingControllerSpring implements BookingController {
    private final BookingService bookingService;

    /**
     * Конструктор для инициализации объекта контроллера бронирования.
     */
    @Autowired
    public BookingControllerSpring(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Создание бронирования", description = "Создает новое бронирование и возвращает его данные.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Бронирование создано"),
            @ApiResponse(responseCode = "409", description = "Конфликт бронирования"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@Parameter(description = "Данные для создания бронирования", required = true) @RequestBody BookingDTO bookingDTO) {
        try {
            BookingDTO booking = bookingService.createBooking(bookingDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (BookingConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Отмена бронирования", description = "Отменяет бронирование по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Бронирование отменено"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@Parameter(description = "ID бронирования для отмены", required = true) @RequestParam(name = "bookingId") long bookingId) {
        try {
            String responseSuccess = "Бронирование отменено";
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).header("Content-Type", "text/plain; charset=UTF-8").body(responseSuccess);
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Получение бронирований пользователя", description = "Возвращает список бронирований пользователя по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирования не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/user")
    public ResponseEntity<?> getBookingsByUser(@Parameter(description = "ID пользователя для получения бронирований", required = true) @RequestParam(name = "userId") long userId) {
        try {
            List<BookingDTO> bookingsByUser = bookingService.getBookingsByUser(userId);
            return ResponseEntity.ok(bookingsByUser);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Получение бронирований пользователя по дате", description = "Возвращает список бронирований пользователя по ID и дате.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирования не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/user/date")
    public ResponseEntity<?> getBookingsByUserAndDate(@Parameter(description = "ID пользователя для получения бронирований", required = true) @RequestParam(name = "userId") long userId, @Parameter(description = "Дата для получения бронирований", required = true) @RequestParam(name = "date") LocalDate date) {
        try {
            List<BookingDTO> bookingsByUserAndDate = bookingService.getBookingsByUserAndDate(userId, date);
            return ResponseEntity.ok(bookingsByUserAndDate);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Получение бронирований пользователя по коворкингу", description = "Возвращает список бронирований пользователя по ID и ID коворкинга.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирования не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/user/coworking")
    public ResponseEntity<?> getBookingsByUserAndCoworking(@Parameter(description = "ID пользователя для получения бронирований", required = true) @RequestParam(name = "userId") long userId, @Parameter(description = "ID коворкинга для получения бронирований", required = true) @RequestParam(name = "coworkingId") long coworkingId) {
        try {
            List<BookingDTO> bookingsByUserAndCoworking = bookingService.getBookingsByUserAndCoworking(userId, coworkingId);
            return ResponseEntity.ok(bookingsByUserAndCoworking);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Получение доступных временных слотов", description = "Возвращает список доступных временных слотов для указанного коворкинга и даты.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/availableslots")
    public ResponseEntity<?> getAvailableSlots(@Parameter(description = "ID коворкинга для получения временных слотов", required = true) @RequestParam(name = "coworkingId") long coworkingId, @Parameter(description = "Дата для получения временных слотов", required = true) @RequestParam(name = "date") LocalDate date) {
        try {
            List<TimeSlot> availableSlots = bookingService.getAvailableSlots(coworkingId, date);
            return ResponseEntity.ok(availableSlots);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Получение бронирования по ID", description = "Возвращает данные бронирования по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<?> getBookingById(@Parameter(description = "ID бронирования для получения", required = true) @RequestParam(name = "bookingId") long bookingId) {
        try {
            BookingDTO bookingById = bookingService.getBookingById(bookingId);
            return ResponseEntity.ok(bookingById);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Обновление бронирования", description = "Обновляет данные существующего бронирования.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "409", description = "Конфликт бронирования"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/update")
    public ResponseEntity<?> updateBooking(@Parameter(description = "Данные для обновления бронирования", required = true) @RequestBody BookingDTO booking) {
        try {
            BookingDTO updatedBooking = bookingService.updateBooking(booking);
            return ResponseEntity.ok(updatedBooking);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BookingConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}