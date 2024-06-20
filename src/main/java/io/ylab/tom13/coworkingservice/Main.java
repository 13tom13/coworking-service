package io.ylab.tom13.coworkingservice;

import io.ylab.tom13.coworkingservice.out.CoworkingService;

/**
 * Главный класс приложения, запускающий CoworkingService.
 */
public class Main {

    /**
     * Основной метод приложения, запускающий CoworkingService.
     */
    public static void main(String[] args) {
        new CoworkingService().start();
    }
}

