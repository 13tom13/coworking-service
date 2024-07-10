package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для управления операциями бронирования.
 */
public interface BookingController {

    /**
     * Создание нового бронирования.
     *
     * @param bookingDTO DTO с данными для бронирования.
     * @return ResponseDTO с информацией о созданном бронировании или сообщением об ошибке.
     */
    ResponseEntity<?> createBooking(BookingDTO bookingDTO);

    /**
     * Отмена существующего бронирования.
     *
     * @param bookingId ID бронирования для отмены.
     * @return ResponseDTO с сообщением об успешной отмене или ошибке.
     */
    ResponseEntity<?> cancelBooking(long bookingId);

    /**
     * Получение бронирований по пользователю.
     *
     * @param userId ID пользователя для получения бронирований.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseEntity<?> getBookingsByUser(long userId);

    /**
     * Получение бронирований по пользователю и дате.
     *
     * @param userId ID пользователя для получения бронирований.
     * @param date   Дата для фильтрации бронирований по дате.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseEntity<?> getBookingsByUserAndDate(long userId, LocalDate date);

    /**
     * Получение бронирований по пользователю и коворкингу.
     *
     * @param userId      ID пользователя для получения бронирований.
     * @param coworkingId ID коворкинга для фильтрации.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseEntity<?> getBookingsByUserAndCoworking(long userId, long coworkingId);

    /**
     * Получение свободных слотов для бронирования по коворкингу и дате.
     *
     * @param coworkingId ID коворкинга для фильтрации свободных слотов.
     * @param date        Дата для фильтрации свободных слотов.
     * @return ResponseDTO с коллекцией доступных временных слотов.
     */
    ResponseEntity<?> getAvailableSlots(long coworkingId, LocalDate date);

    /**
     * Получение бронирования по ID.
     *
     * @param bookingId ID искомого бронирования.
     * @return ResponseDTO с найденным бронированием или сообщением об ошибке.
     */
    ResponseEntity<?> getBookingById(long bookingId);

    /**
     * Обновление информации о бронировании.
     *
     * @param booking данные бронирования для внесения изменений.
     * @return ResponseDTO с обновленными данными бронирования или сообщением об ошибке.
     */
    ResponseEntity<?> updateBooking(BookingDTO booking);
}
