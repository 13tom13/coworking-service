package io.ylab.tom13.coworkingservice.out.menu.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.client.user.UserEditClient;
import io.ylab.tom13.coworkingservice.out.exceptions.EditException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.utils.Session;
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
            UserDTO user = (UserDTO) Session.getInstance().getAttribute("user");
            System.out.printf("Редактировать пользователя: %s %n", user);
            System.out.println("1. Изменить имя пользователя");
            System.out.println("2. Изменить фамилию пользователя");
            System.out.println("3. Изменить email пользователя");
            System.out.println("4. Изменить пароль пользователя");
            System.out.println("5. Выход из редактирования");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> {
                    String newFirstName  = readString("Введите новое имя:");
                    userEditClient.editUser(
                            new UserDTO(user.id(), newFirstName, user.lastName(),user.email()));
                }
                case 2 -> {
                    String newLastName = readString("Введите новую фамилию:");
                    userEditClient.editUser(
                            new UserDTO(user.id(), user.firstName(), newLastName,user.email()));
                }
                case 3 -> {
                    String newEmail= readString("Введите новый email:");
                    userEditClient.editUser(
                            new UserDTO(user.id(), user.firstName(), user.lastName(),newEmail));
                }
                case 4 -> {
                    String oldPassword = readString("Введите старый пароль:");
                    String newHashPassword = BCrypt.hashpw(readString("Введите новый пароль:"), BCrypt.gensalt());
                    userEditClient.editPassword(user.email(), oldPassword, newHashPassword);
                }
                case 5 -> {
                    System.err.println("Выход из редактирования");
                    userManagementMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

}
