package io.ylab.tom13.coworkingservice.out.menu.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.utils.Session;

public class UserMenu extends Menu {

    private final UserEditMenu userEditMenu;

    public UserMenu() {
        userEditMenu = new UserEditMenu();
    }

    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            UserDTO user = (UserDTO) Session.getInstance().getAttribute("user");
            System.out.printf("Добро пожаловать: %s %n", user);
            System.out.println("Выберите действие:");
            System.out.println("1. Управление бронированиями");
            System.out.println("2. Редактирование профиля");
            System.out.println("3. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> System.out.println("Управление бронированиями");
                case 2 -> userEditMenu.display();
                case 3 -> {
                    System.err.println("До свидания");
                    Session.getInstance().removeAttribute("user");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }
}
