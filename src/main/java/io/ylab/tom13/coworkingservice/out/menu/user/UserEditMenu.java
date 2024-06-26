package io.ylab.tom13.coworkingservice.out.menu.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.client.UserEditClient;
import io.ylab.tom13.coworkingservice.out.exceptions.EditException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import org.mindrot.jbcrypt.BCrypt;

public class UserEditMenu extends Menu {

    private final UserEditClient userEditClient;

    public UserEditMenu() {
        userEditClient = new UserEditClient();
    }

    @Override
    public void display() {
        try {
            editUser();
        } catch (EditException e) {
            System.err.println(e.getMessage());
        }
    }

    private void editUser() throws EditException {
        boolean userManagementMenu = true;
        while (userManagementMenu) {
            UserDTO user = (UserDTO) localSession.getAttribute("user");
            System.out.printf("Редактировать пользователя: %s %n", user);
            System.out.println("1. Изменить имя пользователя");
            System.out.println("2. Изменить фамилию пользователя");
            System.out.println("3. Изменить email пользователя");
            System.out.println("4. Изменить пароль пользователя");
            System.out.println("0. Выход из редактирования");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> editFirstName(user);
                case 2 -> editLastName(user);
                case 3 -> editEmail(user);
                case 4 -> editPassword(user);
                case 0 -> {
                    System.err.println("Выход из редактирования");
                    userManagementMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    private void editFirstName(UserDTO user) throws EditException {
        String newFirstName = readString("Введите новое имя:");
        userEditClient.editUser(
                new UserDTO(user.id(), newFirstName, user.lastName(), user.email(), user.role()));
    }

    private void editLastName(UserDTO user) throws EditException {
        String newLastName = readString("Введите новую фамилию:");
        userEditClient.editUser(
                new UserDTO(user.id(), user.firstName(), newLastName, user.email(), user.role()));
    }

    private void editEmail(UserDTO user) throws EditException {
        String newEmail = readString("Введите новый email:");
        userEditClient.editUser(
                new UserDTO(user.id(), user.firstName(), user.lastName(), newEmail, user.role()));
    }

    private void editPassword(UserDTO user) throws EditException {
        String oldPassword = readString("Введите старый пароль:");
        String newHashPassword = readPassword();
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(user.email(), oldPassword, newHashPassword);
        String response = userEditClient.editPassword(passwordChangeDTO);
        System.out.println(response);
    }

}
