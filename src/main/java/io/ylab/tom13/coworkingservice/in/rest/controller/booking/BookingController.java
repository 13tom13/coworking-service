package io.ylab.tom13.coworkingservice.in.rest.controller.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface BookingController {

    /**
     * Создание нового бронирования.
     *
     * @param bookingDTO DTO с данными для бронирования.
     * @return ResponseDTO с информацией о созданном бронировании или сообщением об ошибке.
     */
    ResponseDTO<BookingDTO> createBooking(BookingDTO bookingDTO);

    /**
     * Отмена существующего бронирования.
     *
     * @param bookingId ID бронирования для отмены.
     * @return ResponseDTO с сообщением об успешной отмене или ошибке.
     */
    ResponseDTO<Void> cancelBooking(long bookingId);


    /**
     * Получение бронирований по пользователю.
     *
     * @param userId ID пользователя для получения бронирований пользователя.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseDTO<List<BookingDTO>> getBookingsByUser(long userId);

    /**
     * Получение бронирований по пользователю и дате.
     *
     * @param userId ID пользователя для получения бронирований пользователя.
     * @param date Дата для фильтрации бронирований по дате.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseDTO<List<BookingDTO>> getBookingsByUserAndDate(long userId, LocalDate date);

    /**
     * Получение бронирований по пользователю и дате.
     *
     * @param userId ID пользователя для получения бронирований пользователя.
     * @param coworkingId ID коворкинга для фильтрации.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseDTO<List<BookingDTO>> getBookingsByUserAndCoworking(long userId, long coworkingId);


    /**
     * Получение свободных слотов для бронирования по дате.
     *
     * @param coworkingId ID пользователя для фильтрации бронирований по коворкингу.
     * @param date Дата для фильтрации бронирований по дате.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseDTO<List<TimeSlot>> getAvailableSlots(long coworkingId, LocalDate date);

    /**
     * Получение бронирования по ID.
     *
     * @param bookingId ID искомого бронирования.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseDTO<BookingDTO> getBookingById(long bookingId);

    /**
     * Внесение изменений в бронирование.
     *
     * @param booking данные бронирования для внесения изменений.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseDTO<BookingDTO> updateBooking(BookingDTO booking);
}

