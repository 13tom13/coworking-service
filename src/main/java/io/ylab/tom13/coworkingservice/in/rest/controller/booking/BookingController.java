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
     * @param userId ID пользователя для фильтрации бронирований.
     * @return ResponseDTO с коллекцией бронирований пользователя.
     */
    ResponseDTO<List<BookingDTO>> getBookingsByUser(long userId);

    /**
     * Получение бронирований по ресурсу.
     *
     * @param coworkingId ID ресурса для фильтрации бронирований.
     * @return ResponseDTO с коллекцией бронирований ресурса.
     */
    ResponseDTO<List<BookingDTO>> getBookingsByCoworking(long coworkingId);


    ResponseDTO<List<TimeSlot>> getAvailableSlots(long coworkingId, LocalDate date);
}

