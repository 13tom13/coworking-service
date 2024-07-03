package io.ylab.tom13.coworkingservice.out.menu.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.menu.booking.BookingMainMenu;

public class UserMenu extends Menu {

    private final UserEditMenu userEditMenu;
    private final BookingMainMenu bookingMainMenu;

    public UserMenu() {
        bookingMainMenu = new BookingMainMenu();
        userEditMenu = new UserEditMenu();
    }

    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            UserDTO user = localSession.getUser();
            System.out.printf("Добро пожаловать %s %n", user);
            System.out.println("Выберите действие:");
            System.out.println("1. Редактирование профиля");
            System.out.println("2. Управление бронированиями");
            System.out.println("0. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> userEditMenu.display();
                case 2 -> bookingMainMenu.display();
                case 0 -> {
                    System.err.println("До свидания " + user.firstName());
                    localSession.removeAttribute("user");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }
}
