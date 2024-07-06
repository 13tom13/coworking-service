package io.ylab.tom13.coworkingservice.out;

import io.ylab.tom13.coworkingservice.in.utils.Session;
import io.ylab.tom13.coworkingservice.out.menu.MainMenu;

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
    }

    /**
     * Метод запуска приложения.
     * Создает экземпляр главного меню (MainMenu) и отображает его.
     */
    public void start() {
        new MainMenu().display();
    }

}

