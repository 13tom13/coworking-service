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
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        Map<String, CoworkingDTO> coworkings = bookingClient.getAllCoworkings();
        Session.getInstance().setAttribute("coworkings", coworkings);
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
        String coworkingName  = readString("Введите название коворкинга:");
        long coworkingId = coworkings.get(coworkingName).getId();
        LocalDate date =correctDate();
        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(coworkingId, date);
        System.out.printf("Доступные слоты на %s:%n", date);
        availableSlots.forEach(System.out::println);
        System.out.println();
    }

    private void createBooking(Map<String, CoworkingDTO> coworkings, UserDTO user)  {
        String coworkingName = readString("Введите название коворкинга:");
        long coworkingId = coworkings.get(coworkingName).getId();
        LocalDate date = readLocalDate("Введите дату бронирования в формате ДД.ММ.ГГ:");

        List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(coworkingId, date);
        System.out.printf("Доступные слоты на %s:%n", date);
        availableSlots.forEach(System.out::println);
        System.out.println();

        LocalTime startTime = readLocalTime("Введите время начала бронирования в формате ЧЧ:ММ:");
        LocalTime endTime = readLocalTime("Введите время окончания бронирования в формате ЧЧ:ММ:");

        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        BookingDTO bookingDTO = new BookingDTO(0, user.id(), coworkingId, startDateTime, endDateTime); // здесь замените userId и id на корректные значения
        try {
            bookingClient.createBooking(bookingDTO);
        } catch (BookingException e) {
            System.err.println(e.getMessage());
        }
    }

    private LocalDate correctDate () {
        while (true)  {
            LocalDate localDate = readLocalDate("Введите дату в формате ДД.ММ.ГГ:");
            if  (localDate.isAfter(LocalDate.now())){
                return localDate;
            } else  {
                System.err.println("Дата должна быть в будущем. Пожалуйста, введите корректную дату.");
            }
        }
    }
}