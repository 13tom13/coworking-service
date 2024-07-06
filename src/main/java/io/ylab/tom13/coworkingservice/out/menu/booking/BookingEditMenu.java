package io.ylab.tom13.coworkingservice.out.menu.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.client.BookingClient;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;
import io.ylab.tom13.coworkingservice.in.utils.Session;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class BookingEditMenu extends BookingMenu {

    private final BookingClient bookingClient;

    public BookingEditMenu() {
        bookingClient = new BookingClient();
    }

    @Override
    public void display() {
        UserDTO user = localSession.getUser();
        boolean editMenu = true;
        BookingDTO booking;
        Map<String, CoworkingDTO> allAvailableCoworkings;
        try {
            allAvailableCoworkings = bookingClient.getAllAvailableCoworkings();
            getBookingForEdit(allAvailableCoworkings, user);
        } catch (BookingException | UnauthorizedException e) {
            System.err.println(e.getMessage());
            return;
        }
        while (editMenu) {
            booking = (BookingDTO) localSession.getAttribute("bookingForEdit");
            String coworkingBookingName = getCoworkingNameById(allAvailableCoworkings, booking.coworkingId());
            System.out.println("Меню редактирования бронирования");
            viewBookingForEdit(booking, coworkingBookingName);
            System.out.println("Выберите действие:");
            System.out.println("1. Изменить время бронирования");
            System.out.println("2. Изменить дату бронирования");
            System.out.println("3. Изменить коворкинг для бронирования");
            System.out.println("4. Удалить бронирование");
            System.out.println("0. Выход из редактирования бронирования");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> editingBookingTime(booking);
                case 2 -> editingBookingDate(booking);
                case 3 -> editingBookingCoworking(booking);
                case 4 -> {
                    deleteBooking(booking);
                    Session.getInstance().removeAttribute("bookingForEdit");
                    Session.getInstance().removeAttribute("CoworkingBookingName");
                    editMenu = false;
                }
                case 0 -> {
                    System.err.println("Выход из меню редактирования бронирования");
                    Session.getInstance().removeAttribute("bookingForEdit");
                    Session.getInstance().removeAttribute("CoworkingBookingName");
                    editMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    private void viewAllUserBookings(Map<String, CoworkingDTO> allAvailableCoworkings, UserDTO user) {
        try {
            List<BookingDTO> allUserBookings = bookingClient.getAllUserBookings(user);
            if (allUserBookings.isEmpty()) {
                System.err.println("У пользователя нет бронирований.");
                return;
            }
            viewBookingsList(allAvailableCoworkings, allUserBookings);
        } catch (BookingException e) {
            System.err.println(e.getMessage());
        }
    }

    private void getBookingForEdit(Map<String, CoworkingDTO> allAvailableCoworkings, UserDTO user) throws BookingException {
        viewAllUserBookings(allAvailableCoworkings, user);
        long bookingId = readLong("Введите ID бронирования для редактирования:");
        BookingDTO bookingById = bookingClient.getBookingById(bookingId);
        localSession.setAttribute("bookingForEdit", bookingById);
    }

    private void viewBookingForEdit(BookingDTO booking, String coworkingBookingName) {
        System.out.printf("%s (%s):%n", coworkingBookingName, booking.date().toString());
        booking.timeSlots().forEach(System.out::println);
        System.out.println();
    }

    private void editingBookingTime(BookingDTO booking) {
        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(booking.coworkingId(), booking.date());
        List<TimeSlot> bookingSlots = booking.timeSlots();
        List<TimeSlot> newBookingSlots = selectSlotsForBooking(new ArrayList<>(availableSlots), new ArrayList<>(bookingSlots), booking.date());
        if (newBookingSlots.isEmpty()) {
            System.err.println("Время бронирования не может быть пустым.");
            System.err.println("Изменение времени бронирования отменено.");
            return;
        } else if (bookingSlots.equals(newBookingSlots)) {
            System.out.println("Изменение времени бронирования отменено.");
            return;
        }
        BookingDTO newBookingDTO = updateBookingDTO(booking, newBookingSlots);
        try {
            BookingDTO updatedBooking = bookingClient.updateBooking(newBookingDTO);
            localSession.setAttribute("bookingForEdit", updatedBooking);
        } catch (BookingException e) {
            System.err.println(e.getMessage());
        }
    }

    private void editingBookingDate(BookingDTO booking) {
        System.out.println("Дата бронирования: " + booking.date().toString());
        System.out.println("Введите новую дату для бронирования:");
        LocalDate newDate = readLocalDate();
        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(booking.coworkingId(), newDate);
        if (!availableSlots.isEmpty()) {
            BookingDTO newBookingDTO = updateBookingDTO(booking, newDate);
            if (new HashSet<>(availableSlots).containsAll(newBookingDTO.timeSlots())) {
                try {
                    BookingDTO updatedBooking = bookingClient.updateBooking(newBookingDTO);
                    localSession.setAttribute("bookingForEdit", updatedBooking);
                } catch (BookingException e) {
                    System.err.println(e.getMessage());
                }
            } else {
                System.out.println("Выбранная дата не содержит все необходимые временные слоты для бронирования.");
                System.out.println("Выбрать новое время для бронирования?");
                System.out.println("1. да");
                System.out.println("2. нет");
                int choice = readInt("Выберите действие:");
                switch (choice) {
                    case 1 -> editingBookingTime(newBookingDTO);
                    case 2 -> System.err.println("Изменение даты бронирования отменено.");
                    default -> System.err.println("Неверный выбор. Изменение даты бронирования отменено.");
                }
            }
        } else {
            System.err.println("Нет времени для бронирования на выбранную дату");
            System.err.println("Изменение даты бронирования отменено.");
        }

    }


    private void editingBookingCoworking(BookingDTO booking) {
        Map<String, CoworkingDTO> coworkings;
        try {
            coworkings = bookingClient.getAllAvailableCoworkings();
        } catch (UnauthorizedException e) {
            System.err.println(e.getMessage());
            return;
        }

        String coworkingName = (String) Session.getInstance().getAttribute("CoworkingBookingName");
        System.out.println("Забронированный коворкинг: " + coworkingName);
        System.out.println("Доступные коворкинги:");
        coworkings.values().forEach(System.out::println);

        long newCoworkingId;
        try {
            newCoworkingId = getCoworkingIdByName(coworkings);
        } catch (BookingException e) {
            System.err.println("Изменение бронирования отменено.");
            return;
        }

        BookingDTO newBookingDTO = updateBookingDTO(booking, newCoworkingId);
        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(newBookingDTO.coworkingId(), newBookingDTO.date());
        if (availableSlots.isEmpty()) {
            System.out.println("Выбранный коворкинг не имеет свободного времени на дату бронирования.");
            System.out.println("Выбрать новую дату для бронирования:");
            System.out.println("1. да");
            System.out.println("2. нет");
            int choice = readInt("Выберите действие:");
            switch (choice) {
                case 1 -> editingBookingDate(newBookingDTO);
                case 2 -> System.err.println("Изменение коворкинга отменено.");
                default -> System.err.println("Неверный выбор. Изменение коворкинга отменено.");
            }
        } else if (new HashSet<>(availableSlots).containsAll(newBookingDTO.timeSlots())) {
            try {
                String coworkingNameById = getCoworkingNameById(coworkings, newCoworkingId);
                BookingDTO updatedBooking = bookingClient.updateBooking(newBookingDTO);
                localSession.setAttribute("bookingForEdit", updatedBooking);
                localSession.setAttribute("CoworkingBookingName", coworkingNameById);
            } catch (BookingException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("Выбранная дата не содержит все необходимые временные слоты для бронирования.");
            System.out.println("Выбрать новое время для бронирования?");
            System.out.println("1. да");
            System.out.println("2. нет");
            int choice = readInt("Выберите действие:");
            switch (choice) {
                case 1 -> editingBookingTime(newBookingDTO);
                case 2 -> System.err.println("Изменение даты бронирования отменено.");
                default -> System.err.println("Неверный выбор. Изменение даты бронирования отменено.");
            }
        }
    }

    private void deleteBooking(BookingDTO booking) {
        System.out.println("Вы уверены, что хотите удалить бронирование?");
        System.out.println("1. да");
        System.out.println("2. нет");
        int choice = readInt("Введите номер действия: ");
        switch (choice) {
            case 1 -> {
                try {
                    bookingClient.deleteBooking(booking.id());
                    System.err.println("Бронирование успешно удалено.");
                } catch (BookingException e) {
                    System.err.println(e.getMessage());
                }
            }
            case 2 -> System.err.println("Удаление бронирования отменено.");
        }
    }

    private BookingDTO updateBookingDTO(BookingDTO booking, List<TimeSlot> newTimeSlots) {
        return BookingDTO.builder()
                .id(booking.id())
                .userId(booking.userId())
                .coworkingId(booking.coworkingId())
                .date(booking.date())
                .timeSlots(newTimeSlots)
                .build();
    }

    private BookingDTO updateBookingDTO(BookingDTO booking, LocalDate newDate) {
        return BookingDTO.builder()
                .id(booking.id())
                .userId(booking.userId())
                .coworkingId(booking.coworkingId())
                .date(newDate)
                .timeSlots(booking.timeSlots())
                .build();
    }

    private BookingDTO updateBookingDTO(BookingDTO booking, long newCoworkingId) {
        return BookingDTO.builder()
                .id(booking.id())
                .userId(booking.userId())
                .coworkingId(newCoworkingId)
                .date(booking.date())
                .timeSlots(booking.timeSlots())
                .build();
    }
}
