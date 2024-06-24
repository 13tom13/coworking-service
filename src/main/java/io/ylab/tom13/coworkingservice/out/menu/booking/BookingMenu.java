package io.ylab.tom13.coworkingservice.out.menu.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.client.BookingClient;
import io.ylab.tom13.coworkingservice.out.menu.Menu;

import java.util.Map;

public class BookingMenu extends Menu {

    private final BookingCreateMenu bookingCreateMenu;
    private final BookingViewMenu bookingViewMenu;
    private final BookingClient bookingClient;


    public BookingMenu() {
        bookingCreateMenu = new BookingCreateMenu();
        bookingViewMenu = new BookingViewMenu();
        bookingClient = new BookingClient();
    }

    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            UserDTO user = (UserDTO) localSession.getAttribute("user");
            Map<String, CoworkingDTO> coworkings = bookingClient.getAllAvailableCoworkings();
            localSession.setAttribute("availableCoworkings", coworkings);
            System.out.printf("Пользователь: %s %n", user);
            System.out.println("Меню бронирования");
            System.out.println("Выберите действие:");
            System.out.println("1. Создание бронирования");
            System.out.println("2. Просмотр бронирований");
            System.out.println("0. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> bookingCreateMenu.display();
                case 2 -> bookingViewMenu.display();
                case 0 -> {
                    System.err.println("Выход из меню бронирования");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }


}
