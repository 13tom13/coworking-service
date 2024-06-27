package io.ylab.tom13.coworkingservice.out.menu.administration.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.client.AdministrationClient;
import io.ylab.tom13.coworkingservice.out.exceptions.EditException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;

import java.util.stream.IntStream;

public class UserEditingAdministratorMenu extends Menu {

    private final AdministrationClient administrationClient;


    public UserEditingAdministratorMenu() {
        administrationClient = new AdministrationClient();
    }

    @Override
    public void display() {
        ;
        try {
            UserDTO user = getUserForEdit();
            localSession.setAttribute("userForEdit", user);
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }
        boolean userManagementMenu = true;

        while (userManagementMenu) {
            UserDTO user = (UserDTO) localSession.getAttribute("userForEdit");
            System.out.printf("Редактировать пользователя: %s %n", user);
            System.out.println("1. Изменить имя пользователя");
            System.out.println("2. Изменить фамилию пользователя");
            System.out.println("3. Изменить email пользователя");
            System.out.println("4. Изменить пароль пользователя");
            System.out.println("5. Изменить роль пользователя");
            System.out.println("0. Выход из редактирования");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            try {
                switch (choice) {
                    case 1 -> editFirstName(user);
                    case 2 -> editLastName(user);
                    case 3 -> editEmail(user);
                    case 4 -> editPassword(user);
                    case 5 -> editRoles(user);
                    case 0 -> {
                        System.err.println("Выход из редактирования");
                        userManagementMenu = false;
                    }
                    default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
                }
            } catch (EditException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private UserDTO getUserForEdit() throws UserNotFoundException {
        String email = readString("Введите email пользователя:");
        return administrationClient.getUserByEmail(email);
    }

    private void editFirstName(UserDTO user) throws EditException {
        String newFirstName = readString("Введите новое имя:");
        UserDTO userDTO = administrationClient.editUserByAdministrator(
                new UserDTO(user.id(), newFirstName, user.lastName(), user.email(), user.role()));
        editUser(userDTO);
    }

    private void editLastName( UserDTO user) throws EditException {
        String newLastName = readString("Введите новую фамилию:");
        UserDTO userDTO = new UserDTO(user.id(), user.firstName(), newLastName, user.email(), user.role());
        editUser(userDTO);
    }

    private void editEmail(UserDTO user) throws EditException {
        String newEmail = readString("Введите новый email:");
        UserDTO userDTO = new UserDTO(user.id(), user.firstName(), user.lastName(), newEmail, user.role());
        editUser(userDTO);
    }

    private void editPassword( UserDTO user) throws EditException {
        String hashPassword = readPassword("Введите новый пароль:");
        long userId = user.id();
        String response = administrationClient.editPasswordByAdministrator(userId, hashPassword);
        System.out.println(response);
    }

    private void editRoles( UserDTO user) throws EditException {
        Role[] roles = Role.values();
        System.out.println("Роль пользователя: " + user.role());
        System.out.println("Возможные роли:");
        IntStream.range(0, roles.length)
                .forEach(i -> System.out.println((i + 1) + ". " + roles[i].getDisplayName()));
        System.out.println("0. Выход");
        int choice = readInt("Выберите роль:  ");
        if (choice < 0 || choice > roles.length) {
            System.err.println("Неверный выбор. Попробуйте еще раз.");
            return;
        }
        if (choice == 0) {
            System.err.println("Отмена изменения роли.");
            return;
        }
        Role role = roles[choice - 1];
        UserDTO userDTO = new UserDTO(user.id(), user.firstName(), user.lastName(), user.email(), role);
        editUser(userDTO);
    }

    private void editUser( UserDTO user) throws EditException {
        UserDTO userDTO = administrationClient.editUserByAdministrator(user);
        localSession.setAttribute("userForEdit", userDTO);
        System.out.println("Пользователь успешно изменен ");
    }

}
