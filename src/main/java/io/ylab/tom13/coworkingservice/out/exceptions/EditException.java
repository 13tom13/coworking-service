package io.ylab.tom13.coworkingservice.out.exceptions;

public class EditException extends Exception {

    public EditException(String message) {
        super("Ошибка при редактировании пользователя: " + message);
    }
}
