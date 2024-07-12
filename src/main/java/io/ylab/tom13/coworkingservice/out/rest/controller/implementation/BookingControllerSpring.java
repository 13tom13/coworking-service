package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

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
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO) {
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
    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@RequestParam(name = "bookingId") long bookingId) {
        try {
            String responseSuccess = "Бронирование отменено";
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseSuccess);
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
    @GetMapping("/user")
    public ResponseEntity<?> getBookingsByUser(@RequestParam(name = "userId") long userId) {
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
    @GetMapping("/user/date")
    public ResponseEntity<?> getBookingsByUserAndDate(@RequestParam(name = "userId") long userId, @RequestParam(name = "date") LocalDate date) {
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
    @GetMapping("/user/coworking")
    public ResponseEntity<?> getBookingsByUserAndCoworking(@RequestParam(name = "userId") long userId, @RequestParam(name = "coworkingId") long coworkingId) {
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
    @GetMapping("/availableslots")
    public ResponseEntity<?> getAvailableSlots(@RequestParam(name = "coworkingId") long coworkingId,@RequestParam(name = "date") LocalDate date) {
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
    @GetMapping
    public ResponseEntity<?> getBookingById(@RequestParam(name = "bookingId") long bookingId) {
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
    @PatchMapping("/update")
    public ResponseEntity<?> updateBooking(@RequestBody BookingDTO booking) {
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

