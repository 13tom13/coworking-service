package io.ylab.tom13.coworkingservice.out.utils.mapper;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.entity.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер для преобразования между объектами Booking и BookingDTO.
 */
@Mapper
public interface BookingMapper {

    /**
     * Получение экземпляра маппера BookingMapper.
     */
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    /**
     * Преобразует объект Booking в BookingDTO.
     *
     * @param booking объект Booking для преобразования.
     * @return соответствующий объект BookingDTO.
     */
    BookingDTO toBookingDTO(Booking booking);

    /**
     * Преобразует объект BookingDTO в Booking.
     *
     * @param bookingDTO объект BookingDTO для преобразования.
     * @return соответствующий объект Booking.
     */
    Booking toBooking(BookingDTO bookingDTO);

    /**
     * Преобразует объект BookingDTO в Booking с указанным идентификатором.
     *
     * @param bookingDTO объект BookingDTO для преобразования.
     * @param id         идентификатор бронирования.
     * @return соответствующий объект Booking с заданным идентификатором.
     */
    @Mapping(source = "id", target = "id")
    Booking toBooking(BookingDTO bookingDTO, long id);
}
