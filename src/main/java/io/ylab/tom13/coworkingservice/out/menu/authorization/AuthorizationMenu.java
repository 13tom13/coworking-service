package io.ylab.tom13.coworkingservice.out.menu.authorization;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.LoginException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.client.authorization.AuthorizationClient;
import io.ylab.tom13.coworkingservice.out.menu.user.UserMenu;

/**
 * Меню авторизации пользователя.
 * Предоставляет интерфейс для входа пользователя в систему через консольное взаимодействие.
 */
public class AuthorizationMenu extends Menu {

    private final AuthorizationClient authorizationClient;
    private final UserMenu userMenu;

    /**
     * Конструктор, инициализирующий клиент для авторизации.
     */
    public AuthorizationMenu() {
        authorizationClient = new AuthorizationClient();
        userMenu = new UserMenu();
    }

    /**
     * Отображает меню авторизации пользователя.
     * Вызывает метод входа пользователя через клиент.
     */
    @Override
    public void display() {
        try {
            login();
            userMenu.display();
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
        authorizationClient.login(authorizationDTO);
    }
}

