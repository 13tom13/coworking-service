package io.ylab.tom13.coworkingservice.out.menu.administration.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.client.AdministrationClient;
import io.ylab.tom13.coworkingservice.out.menu.booking.BookingMenu;
import io.ylab.tom13.coworkingservice.out.menu.booking.BookingViewMenu;

public class BookingAdministrationMenu extends BookingMenu {

    private final BookingCreationAdministratorMainMenu bookingCreationAdministratorMenu;
    private final BookingEditingAdministratorMenu bookingEditingAdministratorMenu;
    private final BookingViewMenu bookingViewMenu;

    private final AdministrationClient administrationClient;

    public BookingAdministrationMenu() {
        bookingCreationAdministratorMenu = new BookingCreationAdministratorMainMenu();
        bookingEditingAdministratorMenu = new BookingEditingAdministratorMenu();
        bookingViewMenu = new BookingViewMenu();
        administrationClient = new AdministrationClient();
    }


    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            System.out.println("Меню управления бронированиями");
            System.out.println("Выберите действие:");
            System.out.println("1. Просмотр бронирований пользователя");
            System.out.println("2. Создать бронирование");
            System.out.println("3. Редактировать бронирование");
            System.out.println("0. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> viewUserBookingsMenu();
                case 2 -> bookingCreationAdministratorMenu.display();
                case 3 -> bookingEditingAdministratorMenu.display();
                case 0 -> {
                    System.err.println("Выход из меню управления пользователями");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    private void viewUserBookingsMenu() {
        UserDTO admin = localSession.getUser().get();
        String email = readString("Введите email пользователя: ");
        try {
            UserDTO user = administrationClient.getUserByEmail(email);
            localSession.setUser(user);
            bookingViewMenu.display();
            localSession.setUser(admin);
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
