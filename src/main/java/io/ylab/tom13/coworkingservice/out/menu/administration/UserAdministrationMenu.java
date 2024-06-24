package io.ylab.tom13.coworkingservice.out.menu.administration;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.client.AdministrationClient;
import io.ylab.tom13.coworkingservice.out.client.RegistrationClient;
import io.ylab.tom13.coworkingservice.out.exceptions.EditException;
import io.ylab.tom13.coworkingservice.out.exceptions.RegistrationException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserAdministrationMenu extends Menu {

    private final AdministrationClient administrationClient;
    private final RegistrationClient registrationClient;

    public UserAdministrationMenu() {
        administrationClient = new AdministrationClient();
        registrationClient = new RegistrationClient();
    }

    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            UserDTO user = (UserDTO) localSession.getAttribute("user");
            AuthenticationDTO authentication = new AuthenticationDTO(user.id());
            if (!user.role().equals(Role.ADMINISTRATOR)) {
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
                case 1 -> getUserList(authentication);
                case 2 -> userRegistration();
                case 3 -> userEditing(authentication);
                case 0 -> {
                    System.err.println("Выход из меню управления пользователями");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    private void userRegistration() {
        String firstName = readString("Введите имя пользователя:");
        String lastName = readString("Введите фамилию:");
        String email = readString("Введите email:");
        String hashPassword = BCrypt.hashpw(readString("Введите пароль:"), BCrypt.gensalt());
        RegistrationDTO registrationDTO = new RegistrationDTO(firstName, lastName, email, hashPassword, Role.USER);
        try {
            registrationClient.registration(registrationDTO);
        } catch (RegistrationException e) {
            System.err.println(e.getMessage());
        }
    }

    private void userEditing(AuthenticationDTO authentication) {
        UserDTO user;
        try {
            user = getUserForEdit(authentication);
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }
        boolean userManagementMenu = true;
        while (userManagementMenu) {
            System.out.printf("Редактировать пользователя: %s %n", user);
            System.out.println("1. Изменить имя пользователя");
            System.out.println("2. Изменить фамилию пользователя");
            System.out.println("3. Изменить email пользователя");
            System.out.println("4. Изменить пароль пользователя");
            System.out.println("0. Выход из редактирования");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            try {
                switch (choice) {
                    case 1 -> editFirstName(authentication, user);
                    case 2 -> editLastName(authentication, user);
                    case 3 -> editEmail(authentication, user);
                    case 4 -> editPassword(authentication, user);
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

    private void getUserList(AuthenticationDTO authentication) {
        List<UserDTO> userDTOList = administrationClient.getUserList(authentication);
        System.out.println("Список пользователей:");
        userDTOList.forEach(System.out::println);
        System.out.println();
    }

    private UserDTO getUserForEdit(AuthenticationDTO authentication) throws UserNotFoundException {

            String email = readString("Введите email пользователя:");
            return administrationClient.getUserByEmail(authentication, email);
    }

    private void editFirstName(AuthenticationDTO authentication, UserDTO user) throws EditException {
        String newFirstName = readString("Введите новое имя:");
        administrationClient.editUserByAdministrator(authentication,
                new UserDTO(user.id(), newFirstName, user.lastName(), user.email(), user.role()));
    }

    private void editLastName(AuthenticationDTO authentication, UserDTO user) throws EditException {
        String newLastName = readString("Введите новую фамилию:");
        administrationClient.editUserByAdministrator(authentication,
                new UserDTO(user.id(), user.firstName(), newLastName, user.email(), user.role()));
    }

    private void editEmail(AuthenticationDTO authentication, UserDTO user) throws EditException {
        String newEmail = readString("Введите новый email:");
        administrationClient.editUserByAdministrator(authentication,
                new UserDTO(user.id(), user.firstName(), user.lastName(), newEmail, user.role()));
    }

    private void editPassword(AuthenticationDTO authentication, UserDTO user) throws EditException {
        String newHashPassword = BCrypt.hashpw(readString("Введите новый пароль:"), BCrypt.gensalt());
        long userId = user.id();
        administrationClient.editPasswordByAdministrator(authentication, userId, newHashPassword);
    }

}
