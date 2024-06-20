package io.ylab.tom13.coworkingservice.out.menu;

import io.ylab.tom13.coworkingservice.out.menu.authorization.AuthorizationMenu;
import io.ylab.tom13.coworkingservice.out.menu.registration.RegistrationMenu;

/**
 * Главное меню приложения, предоставляющее пользователю выбор действий: авторизация, регистрация или выход.
 */
public class MainMenu extends Menu {

    private final RegistrationMenu registrationMenu;
    private final AuthorizationMenu authorizationMenu;

    /**
     * Конструктор, инициализирующий главное меню с подменю регистрации и авторизации.
     */
    public MainMenu() {
        registrationMenu = new RegistrationMenu();
        authorizationMenu = new AuthorizationMenu();
    }

    /**
     * Отображает главное меню и обрабатывает выбор пользователя.
     * В цикле предлагает выбрать действие: авторизация, регистрация или выход.
     * Выполняет соответствующие действия в зависимости от выбора пользователя.
     */
    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            System.out.println("Выберите действие:");
            System.out.println("1. Авторизация");
            System.out.println("2. Регистрация");
            System.out.println("3. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия: ");

            switch (choice) {
                case 1 -> authorizationMenu.display();
                case 2 -> registrationMenu.display();
                case 3 -> {
                    System.err.println("Завершение программы");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }
}

