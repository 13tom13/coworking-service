package io.ylab.tom13.coworkingservice.out.menu.administration;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.menu.administration.booking.BookingAdministrationMenu;
import io.ylab.tom13.coworkingservice.out.menu.administration.coworking.CoworkingAdministrationMenu;
import io.ylab.tom13.coworkingservice.out.menu.administration.user.UserAdministrationMenu;

public class AdminMenu extends Menu {

    private final CoworkingAdministrationMenu coworkingAdministrationMenu;
    private final UserAdministrationMenu userAdministrationMenu;
    private final BookingAdministrationMenu bookingAdministrationMenu;

    public AdminMenu() {
        coworkingAdministrationMenu = new CoworkingAdministrationMenu();
        userAdministrationMenu = new UserAdministrationMenu();
        bookingAdministrationMenu = new BookingAdministrationMenu();
    }

    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            UserDTO user = localSession.getUser();
            String name = "Пользователь";
            if (user.role().equals(Role.ADMINISTRATOR)) {
                name = "Администратор";
            } else if (user.role().equals(Role.MODERATOR)) {
                name = "Модератор";
            }
            System.out.printf("Добро пожаловать %s %n", name);
            System.out.println("Выберите действие:");
            System.out.println("1. Управление коворкингами");
            System.out.println("2. Управление бронированиями");
            if (user.role().equals(Role.ADMINISTRATOR)) {
                System.out.println("3. Управление пользователями");
            }
            System.out.println("0. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия:");
            switch (choice) {
                case 1 -> coworkingAdministrationMenu.display();
                case 2 -> bookingAdministrationMenu.display();
                case 3 -> userAdministrationMenu.display();
                case 0 -> {
                    System.err.println("До свидания");
                    localSession.removeUser();
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

}
