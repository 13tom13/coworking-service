package io.ylab.tom13.coworkingservice.out.exceptions;

public class BookingException extends Exception {

    public BookingException(String message) {
        super("Ошибка бронирования: "  + message);
    }
}
