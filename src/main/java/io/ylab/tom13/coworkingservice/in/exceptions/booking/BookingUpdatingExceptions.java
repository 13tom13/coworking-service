package io.ylab.tom13.coworkingservice.in.exceptions.booking;

public class BookingUpdatingExceptions extends Exception {

    public BookingUpdatingExceptions(String message) {
        super("Ошибка при изменении бронирования: " + message);
    }
}
