package io.ylab.tom13.coworkingservice.out.menu.administration.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.client.AdministrationClient;
import io.ylab.tom13.coworkingservice.out.exceptions.EditException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;

import java.util.stream.IntStream;

public class UserAdminRegistrationMenu extends Menu {

    private final AdministrationClient administrationClient;

    public UserAdminRegistrationMenu() {
        administrationClient = new AdministrationClient();
    }

    @Override
    public void display() {
        String firstName = readString("Введите имя пользователя:");
        String lastName = readString("Введите фамилию:");
        String email = readString("Введите email:");
        String hashPassword = readPassword("Введите пароль:");
        Role role = readRole();
        RegistrationDTO registrationDTO = new RegistrationDTO(firstName, lastName, email, hashPassword);
        try {
            administrationClient.registrationUser(registrationDTO, role);
        } catch (EditException e) {
            System.err.println(e.getMessage());
        }
    }

    private Role readRole() {
        Role[] roles = Role.values();
        System.out.println("Выберите роль пользователя:");
        while (true) {
            IntStream.range(0, roles.length).forEach(i -> System.out.println((i + 1) + ". " + roles[i].getDisplayName()));
            System.out.println("0. Выход");
            int choice = readInt("Выберите роль:  ");
            if (choice < 0 || choice > roles.length) {
                System.err.println("Неверный выбор. Попробуйте еще раз.");
                continue;
            }
            if (choice == 0) {
                System.err.println("Отмена изменения роли.");
                continue;
            }
            return roles[choice - 1];
        }
    }
}
