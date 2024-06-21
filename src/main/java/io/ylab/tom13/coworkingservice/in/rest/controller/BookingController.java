package io.ylab.tom13.coworkingservice.in.rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingController {

    /**
     * Создание нового бронирования.
     *
     * @param bookingDTO DTO с данными для бронирования.
     * @return ResponseDTO с информацией о созданном бронировании или сообщением об ошибке.
     */
    ResponseDTO<Booking> createBooking(BookingDTO bookingDTO);

    /**
     * Отмена существующего бронирования.
     *
     * @param bookingId ID бронирования для отмены.
     * @return ResponseDTO с сообщением об успешной отмене или ошибке.
     */
    ResponseDTO<Void> cancelBooking(long bookingId);

    /**
     * Получение всех бронирований.
     *
     * @return ResponseDTO с коллекцией всех бронирований.
     */
    ResponseDTO<Collection<Booking>> getAllBookings();

    /**
     * Получение бронирований по пользователю.
     *
     * @param userId ID пользователя для фильтрации бронирований.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseDTO<Collection<Booking>> getBookingsByUser(long userId);

    /**
     * Получение бронирований по ресурсу.
     *
     * @param spaceId ID ресурса для фильтрации бронирований.
     * @return ResponseDTO с коллекцией бронирований ресурса.
     */
    ResponseDTO<Collection<Booking>> getBookingsBySpace(long spaceId);

    /**
     * Получение бронирований на определённую дату.
     *
     * @param date Дата для фильтрации бронирований.
     * @return ResponseDTO с коллекцией бронирований на указанную дату.
     */
    ResponseDTO<Collection<Booking>> getBookingsByDate(LocalDateTime date);
}

