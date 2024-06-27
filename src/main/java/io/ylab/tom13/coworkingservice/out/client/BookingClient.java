package io.ylab.tom13.coworkingservice.out.client;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.controller.BookingController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.BookingControllerImpl;
import io.ylab.tom13.coworkingservice.in.rest.controller.CoworkingController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.CoworkingControllerImpl;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BookingClient extends Client {


    private final BookingController bookingController;

    private final CoworkingController coworkingController;

    public BookingClient() {
        bookingController = new BookingControllerImpl();
        coworkingController = new CoworkingControllerImpl();
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

    public List<BookingDTO> getUserBookingsByCoworking(long coworkingId, UserDTO user) throws BookingException {
        ResponseDTO<List<BookingDTO>> bookingsByUserAndCoworking = bookingController.getBookingsByUserAndCoworking(user.id(), coworkingId);
        if (bookingsByUserAndCoworking.success()) {
            return bookingsByUserAndCoworking.data();
        } else  {
            throw new BookingException(bookingsByUserAndCoworking.message());
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

    public BookingDTO updateBooking(BookingDTO booking) throws BookingException{
        ResponseDTO<BookingDTO> responseDTO = bookingController.updateBooking(booking);
        if(responseDTO.success()){
            return responseDTO.data();
        } else   {
            throw new BookingException(responseDTO.message());
        }
    }

    public void deleteBooking(long id) throws BookingException {
        ResponseDTO<Void> responseDTO = bookingController.cancelBooking(id);
        if (!responseDTO.success())   {
            throw new BookingException("Не удалось отменить бронирование - " + responseDTO.message());
        }
    }

    public Map<String, CoworkingDTO> getAllAvailableCoworkings() throws UnauthorizedException {
        ResponseDTO<Map<String, CoworkingDTO>> allAvailableCoworking = coworkingController.getAllAvailableCoworkings();
        if (allAvailableCoworking.success()) {
            return allAvailableCoworking.data();
        } else {
            throw new UnauthorizedException(allAvailableCoworking.message());
        }
    }
}
