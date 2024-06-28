package io.ylab.tom13.coworkingservice.out;

import io.ylab.tom13.coworkingservice.out.menu.MainMenu;
import io.ylab.tom13.coworkingservice.out.utils.Session;

import static io.ylab.tom13.coworkingservice.in.config.ApplicationConfig.liquibaseMigrations;

/**
 * Основной класс приложения CoworkingServiceApplication.
 * Инициализирует сеанс и запускает главное меню.
 */
public class CoworkingServiceApplication {

    /**
     * Конструктор класса CoworkingServiceApplication.
     * Инициализирует сеанс (Session).
     */
    public CoworkingServiceApplication() {
        Session.getInstance();
        liquibaseMigrations();
    }

    /**
     * Метод запуска приложения.
     * Создает экземпляр главного меню (MainMenu) и отображает его.
     */
    public void start() {
        new MainMenu().display();
    }

}

