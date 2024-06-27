package io.ylab.tom13.coworkingservice.out.menu.authorization;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.LoginException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.client.AuthorizationClient;
import io.ylab.tom13.coworkingservice.out.menu.administration.AdminMenu;
import io.ylab.tom13.coworkingservice.out.menu.user.UserMenu;

/**
 * Меню авторизации пользователя.
 * Предоставляет интерфейс для входа пользователя в систему через консольное взаимодействие.
 */
public class AuthorizationMenu extends Menu {

    private final AuthorizationClient authorizationClient;
    private final UserMenu userMenu;
    private final AdminMenu adminMenu;

    /**
     * Конструктор, инициализирующий клиент для авторизации.
     */
    public AuthorizationMenu() {
        authorizationClient = new AuthorizationClient();
        userMenu = new UserMenu();
        adminMenu = new AdminMenu();
    }

    /**
     * Отображает меню авторизации пользователя.
     * Вызывает метод входа пользователя через клиент.
     */
    @Override
    public void display() {
        try {
            login();
            UserDTO userDTO = localSession.getUser();
            switch (userDTO.role()) {
                case USER -> userMenu.display();
                case ADMINISTRATOR, MODERATOR -> adminMenu.display();
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }

        } catch (LoginException e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * Метод для выполнения процесса входа пользователя в систему.
     * Считывает email и пароль пользователя с консоли и передает их клиенту для выполнения входа.
     */
    private void login() throws LoginException {
        String email = readString("Введите email:");
        String password = readString("Введите пароль:");
        AuthorizationDTO authorizationDTO = new AuthorizationDTO(email, password);
        UserDTO loginUser = authorizationClient.login(authorizationDTO);
        localSession.setUser(loginUser);
    }
}

