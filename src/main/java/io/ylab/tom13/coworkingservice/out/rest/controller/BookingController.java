package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

/**
 * Контроллер для управления операциями бронирования.
 */
public interface BookingController {

    /**
     * Создание нового бронирования.
     *
     * @param bookingDTO DTO с данными для бронирования.
     * @return ResponseEntity с информацией о созданном бронировании или сообщением об ошибке.
     */
    ResponseEntity<?> createBooking(BookingDTO bookingDTO);

    /**
     * Отмена существующего бронирования.
     *
     * @param bookingId ID бронирования для отмены.
     * @return ResponseEntity с сообщением об успешной отмене или ошибке.
     */
    ResponseEntity<?> cancelBooking(long bookingId);

    /**
     * Получение бронирований по пользователю.
     *
     * @param userId ID пользователя для получения бронирований.
     * @return ResponseEntity с коллекцией бронирований пользователя.
     */
    ResponseEntity<?> getBookingsByUser(long userId);

    /**
     * Получение бронирований по пользователю и дате.
     *
     * @param userId ID пользователя для получения бронирований.
     * @param date   Дата для фильтрации бронирований по дате.
     * @return ResponseEntity с коллекцией бронирований пользователя.
     */
    ResponseEntity<?> getBookingsByUserAndDate(long userId, LocalDate date);

    /**
     * Получение бронирований по пользователю и коворкингу.
     *
     * @param userId      ID пользователя для получения бронирований.
     * @param coworkingId ID коворкинга для фильтрации.
     * @return ResponseEntity с коллекцией бронирований пользователя.
     */
    ResponseEntity<?> getBookingsByUserAndCoworking(long userId, long coworkingId);

    /**
     * Получение свободных слотов для бронирования по коворкингу и дате.
     *
     * @param coworkingId ID коворкинга для фильтрации свободных слотов.
     * @param date        Дата для фильтрации свободных слотов.
     * @return ResponseEntity с коллекцией доступных временных слотов.
     */
    ResponseEntity<?> getAvailableSlots(long coworkingId, LocalDate date);

    /**
     * Получение бронирования по ID.
     *
     * @param bookingId ID искомого бронирования.
     * @return ResponseEntity с найденным бронированием или сообщением об ошибке.
     */
    ResponseEntity<?> getBookingById(long bookingId);

    /**
     * Обновление информации о бронировании.
     *
     * @param booking данные бронирования для внесения изменений.
     * @return ResponseEntity с обновленными данными бронирования или сообщением об ошибке.
     */
    ResponseEntity<?> updateBooking(BookingDTO booking);
}
