package io.ylab.tom13.coworkingservice.out.menu.administration.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.client.AdministrationClient;
import io.ylab.tom13.coworkingservice.out.menu.Menu;

import java.util.List;

public class UserAdministrationMenu extends Menu {
    ;
    private final AdministrationClient administrationClient;

    private final UserEditingAdministratorMenu userEditingAdministratorMenu;
    private final UserAdminRegistrationMenu userAdminRegistrationMenu;

    public UserAdministrationMenu() {
        userEditingAdministratorMenu = new UserEditingAdministratorMenu();
        administrationClient = new AdministrationClient();
        userAdminRegistrationMenu = new UserAdminRegistrationMenu();
    }

    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            UserDTO admin = localSession.getUser();
            if (!admin.role().equals(Role.ADMINISTRATOR)) {
                System.err.println("Редактирование пользователя вам не доступно.");
                startMenu = false;
            }
            System.out.println("Меню управления пользователями");
            System.out.println("Выберите действие:");
            System.out.println("1. Вывести список пользователей");
            System.out.println("2. Зарегистрировать пользователя");
            System.out.println("3. Редактировать пользователя");
            System.out.println("0. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> getUserList();
                case 2 -> userAdminRegistrationMenu.display();
                case 3 -> userEditingAdministratorMenu.display();
                case 0 -> {
                    System.err.println("Выход из меню управления пользователями");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }


    private void getUserList() {
        List<UserDTO> userDTOList = administrationClient.getUserList();
        System.out.println("Список пользователей:");
        userDTOList.forEach(System.out::println);
        System.out.println();
    }
}
