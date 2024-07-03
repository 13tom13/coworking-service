package io.ylab.tom13.coworkingservice.out.menu.administration.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.client.AdministrationClient;
import io.ylab.tom13.coworkingservice.out.client.BookingClient;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;
import io.ylab.tom13.coworkingservice.out.menu.booking.BookingMenu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookingCreationAdministratorMainMenu extends BookingMenu {

    private final AdministrationClient administrationClient;
    private final BookingClient bookingClient;

    public BookingCreationAdministratorMainMenu() {
        bookingClient = new BookingClient();
        administrationClient = new AdministrationClient();
    }

    @Override
    public void display() {
        try {
            UserDTO user = getUserForCreationBooking();
            Map<String, CoworkingDTO> coworkings = bookingClient.getAllAvailableCoworkings();
            createBooking(coworkings, user);
        } catch (UserNotFoundException | UnauthorizedException e) {
            System.err.println(e.getMessage());
        }
    }

    private UserDTO getUserForCreationBooking() throws UserNotFoundException {
        String email = readString("Введите email пользователя для создания бронирования: ");
        return administrationClient.getUserByEmail(email);
    }

    private void createBooking(Map<String, CoworkingDTO> coworkings, UserDTO user) {
        long coworkingId;
        try {
            coworkingId = getCoworkingIdByName(coworkings);
        } catch (BookingException e) {
            System.err.println("Бронирование отменено.");
            return;
        }
        LocalDate date = correctDate();

        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(coworkingId, date);
        List<TimeSlot> selectedSlots = selectSlotsForBooking(new ArrayList<>(availableSlots), new ArrayList<>(), date);

        if (selectedSlots.isEmpty()) {
            System.err.println("Слоты для бронирования не выбраны.");
            System.err.println("Бронирование отменено.");
            return;
        }

        BookingDTO bookingDTO = BookingDTO.builder()
                .userId(user.id())
                .coworkingId(coworkingId)
                .date(date)
                .timeSlots(selectedSlots)
                .build();

        try {
            bookingClient.createBooking(bookingDTO);
            System.out.println("Бронирование успешно создано.");
        } catch (BookingException e) {
            System.err.println(e.getMessage());
        }
    }
}
