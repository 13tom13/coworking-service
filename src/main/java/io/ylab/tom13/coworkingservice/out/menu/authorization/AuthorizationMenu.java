package io.ylab.tom13.coworkingservice.out.menu.authorization;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.client.authorization.AuthorizationClient;

/**
 * Меню авторизации пользователя.
 * Предоставляет интерфейс для входа пользователя в систему через консольное взаимодействие.
 */
public class AuthorizationMenu extends Menu {

    private final AuthorizationClient authorizationClient;

    /**
     * Конструктор, инициализирующий клиент для авторизации.
     */
    public AuthorizationMenu() {
        authorizationClient = new AuthorizationClient();
    }

    /**
     * Отображает меню авторизации пользователя.
     * Вызывает метод входа пользователя через клиент.
     */
    @Override
    public void display() {
        login();
    }

    /**
     * Метод для выполнения процесса входа пользователя в систему.
     * Считывает email и пароль пользователя с консоли и передает их клиенту для выполнения входа.
     */
    private void login() {
        String email = readString("Введите email:");
        String password = readString("Введите пароль:");
        AuthorizationDTO authorizationDTO = new AuthorizationDTO(email, password);
        authorizationClient.login(authorizationDTO);
    }
}

