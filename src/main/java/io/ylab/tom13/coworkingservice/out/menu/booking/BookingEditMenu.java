package io.ylab.tom13.coworkingservice.out.menu.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.out.client.booking.BookingClient;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.utils.Session;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class BookingEditMenu extends Menu {

    private final BookingClient bookingClient;

    public BookingEditMenu() {
        bookingClient = new BookingClient();
    }


    @Override
    public void display() {
        boolean editMenu = true;
        while (editMenu) {
            BookingDTO booking = (BookingDTO) Session.getInstance().getAttribute("bookingForEdit");
            String coworkingBookingName = (String) Session.getInstance().getAttribute("CoworkingBookingName");
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

    private void viewBookingForEdit(BookingDTO booking, String coworkingBookingName) {
        System.out.printf("%s (%s):%n ", coworkingBookingName, booking.date().toString());
        booking.timeSlots().forEach(System.out::println);
        System.out.println();
    }

    private void editingBookingTime(BookingDTO booking) {
        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(booking.coworkingId(), booking.date());
        List<TimeSlot> selectedSlots = selectSlotsForBooking(availableSlots, booking.date());
        if (selectedSlots.isEmpty()) {
            System.out.println("Изменение времени бронирования отменено.");
            return;
        }
        BookingDTO newBookingDTO = updateBookingDTO(booking, selectedSlots);
        try {
            bookingClient.updateBooking(newBookingDTO);
        } catch (BookingException e) {
            System.err.println(e.getMessage());
        }
    }

    private void editingBookingDate(BookingDTO booking) {
        System.out.println("Введите новую дату для бронирования:");
        LocalDate newDate = readLocalDate();
        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(booking.coworkingId(), newDate);
        if (!availableSlots.isEmpty()) {
            BookingDTO newBookingDTO = updateBookingDTO(booking, newDate);
            if (new HashSet<>(availableSlots).containsAll(newBookingDTO.timeSlots())) {
                try {
                    bookingClient.updateBooking(newBookingDTO);
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
        Map<String, CoworkingDTO> coworkings = (Map<String, CoworkingDTO>) Session.getInstance().getAttribute("coworkings");
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
                bookingClient.updateBooking(newBookingDTO);
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

        private BookingDTO updateBookingDTO (BookingDTO booking, List < TimeSlot > newTimeSlots){
            return BookingDTO.builder()
                    .userId(booking.userId())
                    .coworkingId(booking.coworkingId())
                    .date(booking.date())
                    .timeSlots(newTimeSlots)
                    .build();
        }

        private BookingDTO updateBookingDTO (BookingDTO booking, LocalDate newDate){
            return BookingDTO.builder()
                    .userId(booking.userId())
                    .coworkingId(booking.coworkingId())
                    .date(newDate)
                    .timeSlots(booking.timeSlots())
                    .build();
        }

        private BookingDTO updateBookingDTO (BookingDTO booking,long newCoworkingId){
            return BookingDTO.builder()
                    .userId(booking.userId())
                    .coworkingId(newCoworkingId)
                    .date(booking.date())
                    .timeSlots(booking.timeSlots())
                    .build();
        }
    }
