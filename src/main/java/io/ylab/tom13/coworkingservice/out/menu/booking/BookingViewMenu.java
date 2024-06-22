package io.ylab.tom13.coworkingservice.out.menu.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.client.booking.BookingClient;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.utils.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BookingViewMenu extends Menu {

    private final BookingClient bookingClient;

    private final BookingEditMenu bookingEditMenu;

    public BookingViewMenu() {
        bookingClient = new BookingClient();
        bookingEditMenu = new BookingEditMenu();
    }

    @Override
    public void display() {
        boolean viewMenu = true;
        Map<String, CoworkingDTO> coworkings = (Map<String, CoworkingDTO>) Session.getInstance().getAttribute("coworkings");
        UserDTO user = (UserDTO) Session.getInstance().getAttribute("user");
        while (viewMenu) {
            System.out.println("Меню просмотра бронирований пользователя");
            System.out.println("Выберите действие:");
            System.out.println("1. Просмотр всех бронирований");
            System.out.println("2. Просмотр бронирований по дате");
            System.out.println("3. Редактирование бронирования");
            System.out.println("4. Выход из просмотра бронирований");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> viewAllUserBookings(coworkings, user);
                case 2 -> viewUserBookingsByDate(coworkings, user);
                case 3 -> getBookingForEdit(coworkings, user);
                case 4 -> {
                    System.err.println("Выход из меню просмотра бронирований");
                    viewMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    private void viewAllUserBookings(Map<String, CoworkingDTO> coworkings, UserDTO user) {
        try {
            List<BookingDTO> allUserBookings = bookingClient.getAllUserBookings(user);
            if (allUserBookings.isEmpty()) {
                System.err.println("У пользователя нет бронирований.");
                return;
            }
            viewBookingsList(coworkings, allUserBookings);
        } catch (BookingException e) {
            System.err.println(e.getMessage());
        }
    }

    private void viewUserBookingsByDate(Map<String, CoworkingDTO> coworkings, UserDTO user) {
        try {
            LocalDate date = readLocalDate();
            List<BookingDTO> allUserBookings = bookingClient.getUserBookingsByDate(user, date);
            if (allUserBookings.isEmpty()) {
                System.err.println("У пользователя нет бронирований на выбранную дату.");
                return;
            }
            viewBookingsList(coworkings, allUserBookings);
        } catch (BookingException e) {
            System.err.println(e.getMessage());
        }
    }


    private void getBookingForEdit(Map<String, CoworkingDTO> coworkings, UserDTO user) {
        viewAllUserBookings(coworkings, user);
        long bookingId  = readLong("Введите ID бронирования для редактирования:");
        try {
            BookingDTO bookingForEdit = bookingClient.getBookingById(bookingId);
            String CoworkingName  = getCoworkingNameById(coworkings,bookingForEdit.coworkingId());
            Session.getInstance().setAttribute("CoworkingBookingName", CoworkingName);
            Session.getInstance().setAttribute("bookingForEdit", bookingForEdit);
            bookingEditMenu.display();
        } catch (BookingException e) {
            System.err.println(e.getMessage());
        }
    }


    private void viewBookingsList(Map<String, CoworkingDTO> coworkings, List<BookingDTO> allUserBookings) {
        for (BookingDTO booking : allUserBookings) {
            String coworkingName = getCoworkingNameById(coworkings, booking.coworkingId());
            System.out.printf("(ID:%s) %s (%s):%n", booking.id(), coworkingName, booking.date().toString());
            booking.timeSlots().forEach(System.out::println);
            System.out.println();
        }
    }

}
