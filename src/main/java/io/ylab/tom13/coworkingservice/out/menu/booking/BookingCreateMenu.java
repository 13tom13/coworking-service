package io.ylab.tom13.coworkingservice.out.menu.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.out.client.booking.BookingClient;
import io.ylab.tom13.coworkingservice.out.exceptions.BookingException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.utils.Session;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookingCreateMenu extends Menu {

    private final BookingClient bookingClient;

    public BookingCreateMenu() {
        bookingClient = new BookingClient();
    }

    @Override
    public void display() {
        boolean createMenu = true;
        Map<String, CoworkingDTO> coworkings = (Map<String, CoworkingDTO>) Session.getInstance().getAttribute("coworkings");
        UserDTO user = (UserDTO) Session.getInstance().getAttribute("user");
        while (createMenu) {
            System.out.println("Меню создания бронирования");
            System.out.println("Выберите действие:");
            System.out.println("1. Просмотр всех коворкингов");
            System.out.println("2. Просмотр доступных слотов для бронирования");
            System.out.println("3. Забронировать коворкинг");
            System.out.println("4. Выход из меню");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> viewAvailableCoworkings(coworkings);
                case 2 -> viewAvailableSlots(coworkings);
                case 3 -> createBooking(coworkings, user);
                case 4 -> {
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
        String coworkingName = readString("Введите название коворкинга:");
        long coworkingId = coworkings.get(coworkingName).getId();
        LocalDate date = correctDate();
        
        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(coworkingId, date);
        if (availableSlots.isEmpty()) {
            System.out.println("Нет доступных слотов для бронирования.");
        }
        viewAvailableSlots(date, availableSlots);
        System.out.println();
    }

    private void createBooking(Map<String, CoworkingDTO> coworkings, UserDTO user) {
        String coworkingName = readString("Введите название коворкинга:");
        CoworkingDTO coworkingDTO = coworkings.get(coworkingName);
        long coworkingId;
        if (coworkingDTO == null) {
            System.err.println("Коворкинг с таким названием не найден.");
            return;
        } else {
            coworkingId = coworkingDTO.getId();
        }
        LocalDate date = correctDate();

        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(coworkingId, date);
        List<TimeSlot> selectedSlots =  selectSlotsForBooking(availableSlots, date);

        if (selectedSlots.isEmpty()) {
            System.out.println("Бронирование отменено.");
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

    private void viewAvailableSlots(LocalDate date, List<TimeSlot> availableSlots) {
        System.out.printf("Доступные слоты на %s:%n", date);
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, availableSlots.get(i));
        }
    }

    private LocalDate correctDate() {
        while (true) {
            LocalDate localDate = readLocalDate("Введите дату в формате ДД.ММ.ГГ:");
            if (localDate.isAfter(LocalDate.now())) {
                return localDate;
            } else {
                System.err.println("Дата должна быть в будущем. Пожалуйста, введите корректную дату.");
            }
        }
    }

    private  List<TimeSlot> selectSlotsForBooking(List<TimeSlot> availableSlots, LocalDate date) {
        List<TimeSlot> selectedSlots = new ArrayList<>();
        while (true) {
            if (availableSlots.isEmpty()) {
                System.out.println("Нет доступных слотов для бронирования.");
                break;
            }
            viewAvailableSlots(date, availableSlots);
            int slotNumber = readInt("Введите номер слота для бронирования (0 для завершения):");
            if (slotNumber == 0) {
                break;
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
        return selectedSlots;
    }
}