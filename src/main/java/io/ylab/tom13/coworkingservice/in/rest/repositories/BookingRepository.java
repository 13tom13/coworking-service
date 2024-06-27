package io.ylab.tom13.coworkingservice.in.rest.repositories;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    /**
     * Создает новое бронирование.
     *
     * @param newBooking объект Booking, представляющий бронирование
     * @return созданное бронирование с присвоенным идентификатором
     */
    Optional<Booking> createBooking(Booking newBooking) throws BookingConflictException, RepositoryException;

    /**
     * Обновляет существующее бронирование.
     *
     * @param booking объект Booking, представляющий бронирование с обновленными данными
     * @return обновленное бронирование
     */
    Optional<Booking>  updateBooking(Booking updatedBooking) throws BookingNotFoundException, BookingConflictException, RepositoryException;

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
    Collection<Booking> getBookingsByUser(long userId) throws BookingNotFoundException;

    /**
     * Получает бронирования по идентификатору пользователя и дате
     *
     * @param userId идентификатор пользователя
     * @param date   дата бронирования
     * @return список бронирований, связанных с пользователем
     */
    Collection<Booking> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException;

    /**
     * Получает бронирования по идентификатору пользователя и дате
     *
     * @param userId идентификатор пользователя
     * @param coworkingId идентификатор коворкинга для фильтрации
     * @return список бронирований, связанных с пользователем
     */
    Collection<Booking> getBookingsByUserAndCoworking(long userId, long coworkingId) throws BookingNotFoundException;

    /**
     * Получает бронирования по идентификатору коворкинга и дате.
     *
     * @param coworkingId идентификатор коворкинга
     * @param date        дата бронирования
     * @return список бронирований, связанных с коворкингом и датой
     */
    Collection<Booking> getBookingsByCoworkingAndDate(long coworkingId, LocalDate date);

    /**
     * Получает бронирования по ID.
     *
     * @param bookingId ID бронирования
     * @return список бронирований, связанных с коворкингом и датой
     */
    Optional<Booking> getBookingById(long bookingId) throws BookingNotFoundException;

    void deleteAllCoworkingBookings(long coworkingID);

}
