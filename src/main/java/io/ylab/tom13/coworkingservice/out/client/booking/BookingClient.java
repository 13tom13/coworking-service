package io.ylab.tom13.coworkingservice.out.client.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.in.rest.controller.booking.BookingController;
import io.ylab.tom13.coworkingservice.in.rest.controller.booking.CoworkingController;
import io.ylab.tom13.coworkingservice.in.rest.controller.booking.implementation.BookingControllerImpl;
import io.ylab.tom13.coworkingservice.in.rest.controller.booking.implementation.CoworkingControllerImpl;
import io.ylab.tom13.coworkingservice.out.client.Client;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BookingClient extends Client {

    private final CoworkingController coworkingController;
    private final BookingController bookingController;

    public BookingClient() {
        coworkingController = new CoworkingControllerImpl();
        bookingController = new BookingControllerImpl();
    }

    public Map<String, CoworkingDTO> getAllCoworkings() {
        ResponseDTO<Map<String, CoworkingDTO>> AllCoworkings = coworkingController.getAllCoworking();
        return AllCoworkings.data();
    }

    public List<BookingDTO> getAllUserBookings(UserDTO userDTO) throws BookingException {
        ResponseDTO<List<BookingDTO>> bookingsByUser = bookingController.getBookingsByUser(userDTO.id());
        if (bookingsByUser.success()) {
            return bookingsByUser.data();
        } else  {
            throw new BookingException(bookingsByUser.message());
        }
    }

    public List<BookingDTO> getUserBookingsByDate(UserDTO userDTO, LocalDate date) throws BookingException   {
        ResponseDTO<List<BookingDTO>> bookingsByUserAndDate = bookingController.getBookingsByUserAndDate(userDTO.id(), date);
        if (bookingsByUserAndDate.success()) {
            return bookingsByUserAndDate.data();
        } else  {
            throw new BookingException(bookingsByUserAndDate.message());
        }
    }

    public List<TimeSlot> getAvailableSlots(long coworkingId, LocalDate date) {
        ResponseDTO<List<TimeSlot>> availableSlots = bookingController.getAvailableSlots(coworkingId, date);
        return availableSlots.data();
    }

    public void createBooking(BookingDTO bookingDTO) throws BookingException {
        ResponseDTO<BookingDTO> response = bookingController.createBooking(bookingDTO);
        if (!response.success())  {
            throw new BookingException("Не удалось создать бронирование - " + response.message());
        }
    }

    public BookingDTO getBookingById(long bookingId) throws BookingException {
        ResponseDTO<BookingDTO> bookingsById = bookingController.getBookingById(bookingId);
        if (bookingsById.success()) {
            return bookingsById.data();
        } else  {
            throw new BookingException(bookingsById.message());
        }
    }

    public void updateBooking(BookingDTO booking) throws BookingException{
        bookingController.updateBooking(booking);
    }

    public void deleteBooking(long id) throws BookingException {
        ResponseDTO<Void> responseDTO = bookingController.cancelBooking(id);
        if (!responseDTO.success())   {
            throw new BookingException("Не удалось отменить бронирование - " + responseDTO.message());
        }
    }
}
