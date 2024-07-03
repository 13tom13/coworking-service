package io.ylab.tom13.coworkingservice.out.menu.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;

public class BookingMainMenu extends BookingMenu {

    private final BookingCreateMenu bookingCreateMenu;
    private final BookingViewMenu bookingViewMenu;
    private final BookingEditMenu bookingEditMenu;


    public BookingMainMenu() {
        bookingCreateMenu = new BookingCreateMenu();
        bookingViewMenu = new BookingViewMenu();
        bookingEditMenu = new BookingEditMenu();
    }

    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            UserDTO user = localSession.getUser();
            System.out.printf("Пользователь: %s %n", user);
            System.out.println("Меню бронирования");
            System.out.println("Выберите действие:");
            System.out.println("1. Создание бронирования");
            System.out.println("2. Просмотр бронирований");
            System.out.println("3. Редактирование бронирований");
            System.out.println("0. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> bookingCreateMenu.display();
                case 2 -> bookingViewMenu.display();
                case 3 -> bookingEditMenu.display();
                case 0 -> {
                    System.err.println("Выход из меню бронирования");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }


}
