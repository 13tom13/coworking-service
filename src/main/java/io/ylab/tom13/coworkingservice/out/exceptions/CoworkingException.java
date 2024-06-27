package io.ylab.tom13.coworkingservice.out.exceptions;

public class CoworkingException extends Exception {

    public CoworkingException(String message) {
        super("Ошибка коворкинга: "  + message);
    }
}
