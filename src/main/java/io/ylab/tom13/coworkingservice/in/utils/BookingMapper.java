package io.ylab.tom13.coworkingservice.in.utils;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDTO toBookingDTO(Booking booking);

    Booking toBooking(BookingDTO bookingDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "bookingDTO.userId", target = "userId")
    @Mapping(source = "bookingDTO.coworkingId", target = "coworkingId")
    @Mapping(source = "bookingDTO.date", target = "date")
    @Mapping(source = "bookingDTO.timeSlots", target = "timeSlots")
    Booking toBooking(BookingDTO bookingDTO, long id);
}
