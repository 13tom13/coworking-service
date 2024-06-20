package io.ylab.tom13.coworkingservice.in.exceptions.security;

/**
 * Исключение, которое выбрасывается при попытке неавторизованного доступа.
 * Обычно выбрасывается, когда указанный email и/или пароль не совпадают с ожидаемыми.
 */
public class UnauthorizedException extends Exception {

    /**
     * Конструктор исключения.
     * Создает исключение с сообщением "Неверный email или пароль".
     */
    public UnauthorizedException() {
        super("Неверный email или пароль");
    }
}

