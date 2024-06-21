package io.ylab.tom13.coworkingservice.out.exceptions;

public class RegistrationException extends Exception {

    public RegistrationException(String message) {
        super("Ошибка регистрации: "  + message);
    }
}
