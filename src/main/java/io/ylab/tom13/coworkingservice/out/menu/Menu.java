package io.ylab.tom13.coworkingservice.out.menu;

import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Базовый класс для меню приложения.
 * Предоставляет методы для считывания данных с консоли.
 */
public abstract class Menu {

    protected static final Scanner scanner = new Scanner(System.in);

    /**
     * Абстрактный метод, который должен быть реализован в подклассах для отображения меню.
     */
    public abstract void display();

    /**
     * Считывает строку с консоли, предварительно выводя заданный приглашающий текст.
     *
     * @param prompt Поясняющий текст для пользователя.
     * @return Введенная пользователем строка (без начальных и конечных пробелов).
     */
    protected String readString(String prompt) {
        String input;
        do {
            System.out.println(prompt);
            input = scanner.nextLine().trim(); // Удаляем лишние пробелы по краям
            if (input.isEmpty()) {
                System.err.println("Неверный ввод. Ввод не должен быть пустым.");
            }
        } while (input.isEmpty());

        return input;
    }

    /**
     * Считывает целое число с консоли, предварительно выводя заданный приглашающий текст.
     *
     * @param prompt Поясняющий текст для пользователя.
     * @return Введенное пользователем целое число.
     */
    protected int readInt(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Неверный ввод. Пожалуйста, введите корректное число.");
            }
        }
    }

    /**
     * Считывает целое число с консоли, предварительно выводя заданный приглашающий текст.
     *
     * @param prompt Поясняющий текст для пользователя.
     * @return Введенное пользователем целое число.
     */
    protected long readLong(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Неверный ввод. Пожалуйста, введите корректное число.");
            }
        }
    }

    /**
     * Считывает дату с консоли в формате "ДД.ММ.ГГ", предварительно выводя заданный приглашающий текст.
     *
     * @return Введенная пользователем дата в формате LocalDate.
     */
    protected LocalDate readLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        while (true) {
            System.out.println("Введите дату в формате ДД.ММ.ГГ:");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.err.println("Неверный формат даты. Пожалуйста, введите дату в формате ДД.ММ.ГГ.");
            }
        }
    }

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


    protected LocalDate correctDate() {
        while (true) {
            LocalDate localDate = readLocalDate();
            if (localDate.isAfter(LocalDate.now())) {
                return localDate;
            } else {
                System.err.println("Дата должна быть в будущем. Пожалуйста, введите корректную дату.");
            }
        }
    }

    protected void displayAvailableSlots(LocalDate date, List<TimeSlot> availableSlots) {
        System.out.printf("Доступные слоты на %s:%n", date);
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, availableSlots.get(i));
        }
    }

    protected void displaySelectedSlots(LocalDate date, List<TimeSlot> selectedSlots) {
        System.out.println("Выбранные слоты на " + date + ":");
        for (int i = 0; i < selectedSlots.size(); i++) {
            System.out.println((i + 1) + ". " + selectedSlots.get(i));
        }
    }

    protected List<TimeSlot> selectSlotsForBooking(List<TimeSlot> availableSlots, LocalDate date) {
        List<TimeSlot> selectedSlots = new ArrayList<>();
        while (true) {
            if (availableSlots.isEmpty() && selectedSlots.isEmpty()) {
                System.out.println("Нет доступных слотов для бронирования на выбранную дату");
                return selectedSlots;
            }
            displayAvailableSlots(date, availableSlots);
            if (!selectedSlots.isEmpty()) displaySelectedSlots(date, selectedSlots);
            System.out.println("Выберите действие:");
            System.out.println("1. Добавить слот");
            System.out.println("2. Удалить слот");
            System.out.println("0. Завершить выбор слотов");

            int action = readInt("Введите номер действия: ");

            switch (action) {
                case 0 -> {
                    return selectedSlots;
                }
                case 1 -> {
                    if (availableSlots.isEmpty()) {
                        System.out.println("Нет доступных слотов для добавления.");
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
                    if (!selectedSlots.contains(selectedSlot)) {
                        availableSlots.remove(selectedSlot);
                        selectedSlots.add(selectedSlot);
                    } else {
                        System.out.println("Слот уже выбран. Пожалуйста, выберите другой слот.");
                    }
                }
                case 2 -> {
                    if (selectedSlots.isEmpty()) {
                        System.out.println("Нет выбранных слотов для удаления.");
                        continue;
                    }

                    int slotNumber = readInt("Введите номер слота для удаления (0 для отмены):");
                    if (slotNumber == 0) {
                        continue;
                    }
                    if (slotNumber < 1 || slotNumber > selectedSlots.size()) {
                        System.out.println("Неверный номер слота. Пожалуйста, попробуйте снова.");
                        continue;
                    }
                    TimeSlot selectedSlot = selectedSlots.get(slotNumber - 1);
                    selectedSlots.remove(selectedSlot);
                    availableSlots.add(selectedSlot);
                }
                default -> System.out.println("Неверное действие. Пожалуйста, попробуйте снова.");

            }
        }
    }
}

