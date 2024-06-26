package io.ylab.tom13.coworkingservice.in.exceptions.security;

public class NoAccessException extends Exception{

    public NoAccessException() {
        super("Отказано в доступе");
    }

    public NoAccessException(String message) {
        super(message);
    }
}
