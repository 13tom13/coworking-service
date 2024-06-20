package io.ylab.tom13.coworkingservice.out;

import io.ylab.tom13.coworkingservice.out.menu.MainMenu;
import io.ylab.tom13.coworkingservice.out.utils.Session;

/**
 * Основной класс приложения CoworkingService.
 * Инициализирует сеанс и запускает главное меню.
 */
public class CoworkingService {

    /**
     * Конструктор класса CoworkingService.
     * Инициализирует сеанс (Session).
     */
    public CoworkingService() {
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

