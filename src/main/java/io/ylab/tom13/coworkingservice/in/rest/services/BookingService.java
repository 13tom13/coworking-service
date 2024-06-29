package io.ylab.tom13.coworkingservice.in.rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для управления операциями бронирования.
 */
public interface BookingService {

    /**
     * Создает новое бронирование.
     *
     * @param bookingDTO DTO с данными для создания бронирования.
     * @return DTO созданного бронирования.
     * @throws BookingConflictException если возникает конфликт при бронировании.
     * @throws RepositoryException      если возникает ошибка репозитория при сохранении данных.
     */
    BookingDTO createBooking(BookingDTO bookingDTO) throws BookingConflictException, RepositoryException;

    /**
     * Отменяет существующее бронирование.
     *
     * @param bookingId ID бронирования для отмены.
     * @throws BookingNotFoundException если бронирование с указанным ID не найдено.
     */
    void cancelBooking(long bookingId) throws BookingNotFoundException, RepositoryException;

    /**
     * Получает список бронирований пользователя.
     *
     * @param userId ID пользователя для получения его бронирований.
     * @return список DTO бронирований пользователя.
     * @throws BookingNotFoundException если не найдено бронирование для указанного пользователя.
     */
    List<BookingDTO> getBookingsByUser(long userId) throws BookingNotFoundException, RepositoryException;

    /**
     * Получает список бронирований пользователя на указанную дату.
     *
     * @param userId ID пользователя для получения его бронирований.
     * @param date   дата для фильтрации бронирований.
     * @return список DTO бронирований пользователя на указанную дату.
     * @throws BookingNotFoundException если не найдено бронирование для указанного пользователя на указанную дату.
     */
    List<BookingDTO> getBookingsByUserAndDate(long userId, LocalDate date) throws BookingNotFoundException, RepositoryException;

    /**
     * Получает список бронирований пользователя в указанном коворкинге.
     *
     * @param userId      ID пользователя для получения его бронирований.
     * @param coworkingId ID коворкинга для фильтрации бронирований.
     * @return список DTO бронирований пользователя в указанном коворкинге.
     * @throws BookingNotFoundException если не найдено бронирование для указанного пользователя в указанном коворкинге.
     */
    List<BookingDTO> getBookingsByUserAndCoworking(long userId, long coworkingId) throws BookingNotFoundException, RepositoryException;

    /**
     * Получает список доступных временных слотов для бронирования в указанный день в указанном коворкинге.
     *
     * @param coworkingId ID коворкинга для фильтрации доступных слотов.
     * @param date        дата для фильтрации доступных слотов.
     * @return список доступных временных слотов в указанный день в указанном коворкинге.
     */
    List<TimeSlot> getAvailableSlots(long coworkingId, LocalDate date) throws RepositoryException;

    /**
     * Получает бронирование по его ID.
     *
     * @param bookingId ID бронирования для получения.
     * @return DTO бронирования с указанным ID.
     * @throws BookingNotFoundException если бронирование с указанным ID не найдено.
     */
    BookingDTO getBookingById(long bookingId) throws BookingNotFoundException, RepositoryException;

    /**
     * Обновляет информацию о существующем бронировании.
     *
     * @param booking DTO с обновленными данными бронирования.
     * @return DTO обновленного бронирования.
     * @throws BookingNotFoundException  если бронирование с указанным ID не найдено.
     * @throws BookingConflictException  если возникает конфликт при обновлении бронирования.
     * @throws RepositoryException       если возникает ошибка репозитория при сохранении данных.
     */
    BookingDTO updateBooking(BookingDTO booking) throws BookingNotFoundException, BookingConflictException, RepositoryException;

}

