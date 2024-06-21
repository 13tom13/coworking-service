package io.ylab.tom13.coworkingservice.out.exceptions;

public class LoginException extends Exception {

    public LoginException(String message) {
        super("Ошибка авторизации: "  + message);
    }
}
