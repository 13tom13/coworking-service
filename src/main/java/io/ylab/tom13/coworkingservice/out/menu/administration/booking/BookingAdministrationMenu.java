package io.ylab.tom13.coworkingservice.out.menu.administration.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.client.AdministrationClient;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.menu.booking.BookingViewMenu;

public class BookingAdministrationMenu extends Menu {

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
            UserDTO admin = (UserDTO) localSession.getAttribute("user");
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
        UserDTO admin = (UserDTO) localSession.getAttribute("user");
        AuthenticationDTO authentication  = new AuthenticationDTO(admin.id());
        String email = readString("Введите email пользователя: ");
        try {
            UserDTO user = administrationClient.getUserByEmail(authentication, email);
            localSession.setAttribute("user", user);
            bookingViewMenu.display();
            localSession.setAttribute("user", admin);
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
