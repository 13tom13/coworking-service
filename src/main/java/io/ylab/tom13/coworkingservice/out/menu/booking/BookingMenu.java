package io.ylab.tom13.coworkingservice.out.menu.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public abstract class BookingMenu extends Menu {

    protected long getCoworkingIdByName(Map<String, CoworkingDTO> coworkings) throws BookingException {
        String coworkingName = readString("Введите название коворкинга:");
        CoworkingDTO coworkingDTO = coworkings.get(coworkingName);
        if (coworkingDTO != null) {
            return coworkingDTO.getId();
        } else {
            throw new BookingException("Коворкинг с таким названием не найден.");
        }
    }

    protected String getCoworkingNameById(Map<String, CoworkingDTO> coworkings, long coworkingId) {
        return coworkings.values().stream()
                .filter(coworking -> coworking.getId() == coworkingId)
                .map(CoworkingDTO::getName)
                .findFirst()
                .orElse("Неизвестный коворкинг");
    }

    protected void displaySelectedSlots(LocalDate date, List<TimeSlot> selectedSlots) {
        if (selectedSlots.isEmpty()) {
            System.out.println("Нет выбранных слотов на " + date);
            return;
        }
        System.out.println("Выбранные слоты на " + date + ":");
        for (int i = 0; i < selectedSlots.size(); i++) {
            System.out.println((i + 1) + ". " + selectedSlots.get(i));
        }
    }

    protected List<TimeSlot> selectSlotsForBooking(List<TimeSlot> availableSlots, List<TimeSlot> bookingSlots, LocalDate date) {
        while (true) {
            displayAvailableSlots(date, availableSlots);
            displaySelectedSlots(date, bookingSlots);
            System.out.println();
            System.out.println("Выберите действие:");
            System.out.println("1. Добавить слот");
            System.out.println("2. Удалить слот");
            System.out.println("0. Завершить выбор слотов");

            int action = readInt("Введите номер действия: ");

            switch (action) {
                case 0 -> {
                    return bookingSlots;
                }
                case 1 -> {
                    if (availableSlots.isEmpty()) {
                        System.err.println("Нет доступных слотов для добавления.");
                        continue;
                    }
                    int slotNumber = readInt("Введите номер слота для бронирования (0 для отмены):");
                    if (slotNumber == 0) {
                        continue;
                    }
                    if (slotNumber < 1 || slotNumber > availableSlots.size()) {
                        System.out.println("Неверный номер слота. Пожалуйста, попробуйте снова.");
                        continue;
                    }
                    TimeSlot selectedSlot = availableSlots.get(slotNumber - 1);
                    availableSlots.remove(selectedSlot);
                    bookingSlots.add(selectedSlot);
                }
                case 2 -> {
                    int slotNumber = readInt("Введите номер слота для удаления (0 для отмены):");
                    if (slotNumber == 0) {
                        continue;
                    }
                    if (slotNumber < 1 || slotNumber > bookingSlots.size()) {
                        System.out.println("Неверный номер слота. Пожалуйста, попробуйте снова.");
                        continue;
                    }
                    TimeSlot selectedSlot = bookingSlots.get(slotNumber - 1);
                    bookingSlots.remove(selectedSlot);
                    availableSlots.add(selectedSlot);
                }
                default -> System.out.println("Неверное действие. Пожалуйста, попробуйте снова.");

            }
        }
    }

    protected void displayAvailableSlots(LocalDate date, List<TimeSlot> availableSlots) {
        if (availableSlots.isEmpty()) {
            System.out.println("Нет доступных слотов на " + date);
            return;
        }
        System.out.printf("Доступные слоты на %s:%n", date);
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, availableSlots.get(i));
        }
    }
    protected void viewBookingsList(Map<String, CoworkingDTO> coworkings, List<BookingDTO> allUserBookings) {
        for (BookingDTO booking : allUserBookings) {
            String coworkingName = getCoworkingNameById(coworkings, booking.coworkingId());
            System.out.printf("(ID:%s)%n%s (%s):%n", booking.id(), coworkingName, booking.date().toString());
            booking.timeSlots().forEach(System.out::println);
            System.out.println();
        }
    }

}
