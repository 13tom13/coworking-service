package io.ylab.tom13.coworkingservice.out.menu;

import io.ylab.tom13.coworkingservice.out.Menu;

public class MainMenu extends Menu {

    @Override
    public void display() {
        System.out.println("Выберите действие:");
        System.out.println("1. Авторизация");
        System.out.println("2. Регистрация");
        System.out.println("3. Выход");

        boolean startMenu = true;
        while (startMenu) {
            int choice = readInt("Введите номер действия: ");

            switch (choice) {
                case 1 -> System.out.println("Авторизация");
                case 2 -> System.out.println("Регистрация");
                case 3 -> {
                    System.out.println("Завершение программы");
                    startMenu = false;
                }
                default -> System.out.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }
}
