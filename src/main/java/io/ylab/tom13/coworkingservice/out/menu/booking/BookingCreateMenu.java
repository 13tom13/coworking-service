package io.ylab.tom13.coworkingservice.out.menu.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.client.BookingClient;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BookingCreateMenu extends BookingMenu {

    private final BookingClient bookingClient;

    public BookingCreateMenu() {
        bookingClient = new BookingClient();
    }

    @Override
    public void display() {
        boolean createMenu = true;
        Map<String, CoworkingDTO> coworkings;
        try {
            coworkings = bookingClient.getAllAvailableCoworkings();
        } catch (UnauthorizedException e) {
            System.err.println(e.getMessage());
            return;
        }
        UserDTO user = localSession.getUser().get();
        while (createMenu) {
            System.out.println("Меню создания бронирования");
            System.out.println("Выберите действие:");
            System.out.println("1. Просмотр всех коворкингов");
            System.out.println("2. Просмотр доступных слотов для бронирования");
            System.out.println("3. Забронировать коворкинг");
            System.out.println("0. Выход из меню");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> viewAvailableCoworkings(coworkings);
                case 2 -> viewAvailableSlots(coworkings);
                case 3 -> createBooking(coworkings, user);
                case 0 -> {
                    System.err.println("Выход из меню создания бронирования");
                    createMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    private void viewAvailableCoworkings(Map<String, CoworkingDTO> coworkings) {
        System.out.println("Доступные коворкинги:");
        coworkings.values().forEach(System.out::println);
        System.out.println();
    }

    private void viewAvailableSlots(Map<String, CoworkingDTO> coworkings) {
        long coworkingId;
        try {
            coworkingId = getCoworkingIdByName(coworkings);
        } catch (BookingException e) {
            System.err.println("Бронирование отменено.");
            return;
        }
        LocalDate date = correctDate();

        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(coworkingId, date);
        if (availableSlots.isEmpty()) {
            System.out.println("Нет доступных слотов для бронирования.");
        }
        displayAvailableSlots(date, availableSlots);
        System.out.println();
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