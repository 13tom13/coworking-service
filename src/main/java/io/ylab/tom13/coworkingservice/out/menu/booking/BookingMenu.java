package io.ylab.tom13.coworkingservice.out.menu.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.utils.Session;

public class BookingMenu extends Menu {

    private final BookingCreateMenu bookingCreateMenu;

    public BookingMenu() {
        bookingCreateMenu = new BookingCreateMenu();
    }
//    private final BookingEditMenu bookingEditMenu;

    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            UserDTO user = (UserDTO) Session.getInstance().getAttribute("user");
            System.out.printf("Пользователь: %s %n", user);
            System.out.println("Меню бронирования");
            System.out.println("Выберите действие:");
            System.out.println("1. Создание бронирования");
            System.out.println("2. Просмотр бронирований");
            System.out.println("3. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> bookingCreateMenu.display();
//                case 2 -> bookingEditMenu.display();
                case 3 -> {
                    System.err.println("Выход из меню бронирования");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }
}
