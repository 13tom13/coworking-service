package io.ylab.tom13.coworkingservice.in.rest.repositories;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository {

    /**
     * Создает новое бронирование.
     *
     * @param booking объект Booking, представляющий бронирование
     * @return созданное бронирование с присвоенным идентификатором
     */
    BookingDTO createBooking(BookingDTO booking) throws BookingConflictException;

    /**
     * Обновляет существующее бронирование.
     *
     * @param booking объект Booking, представляющий бронирование с обновленными данными
     * @return обновленное бронирование
     */
    BookingDTO updateBooking(BookingDTO booking) throws BookingNotFoundException, BookingConflictException;

    /**
     * Удаляет бронирование по идентификатору.
     *
     * @param bookingId идентификатор бронирования
     */
    void deleteBooking(long bookingId) throws BookingNotFoundException;

    /**
     * Получает бронирования по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список бронирований, связанных с пользователем
     */
    List<BookingDTO> getBookingsByUser(long userId) throws BookingNotFoundException;

    /**
     * Получает бронирования по идентификатору коворкинга.
     *
     * @param coworkingId идентификатор коворкинга
     * @return список бронирований, связанных с коворкингом
     */
    List<BookingDTO> getBookingsByCoworking(long coworkingId) throws BookingNotFoundException;

    /**
     * Получает бронирования по идентификатору коворкинга и дате.
     *
     * @param coworkingId идентификатор коворкинга
     * @param date        дата бронирования
     * @return список бронирований, связанных с коворкингом и датой
     */
    List<BookingDTO> getBookingsByCoworkingAndDate(long coworkingId, LocalDate date);

}
