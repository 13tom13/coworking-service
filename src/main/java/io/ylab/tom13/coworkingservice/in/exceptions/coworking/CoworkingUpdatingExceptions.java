package io.ylab.tom13.coworkingservice.in.exceptions.coworking;

public class CoworkingUpdatingExceptions extends Exception {

    public CoworkingUpdatingExceptions(String message) {
        super("Ошибка при изменении коворкинга: " + message);
    }

}