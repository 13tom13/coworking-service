package io.ylab.tom13.coworkingservice.out.exceptions.security;

/**
 * Исключение, которое выбрасывается при попытке неавторизованного доступа.
 * Обычно выбрасывается, когда указанный email и/или пароль не совпадают с ожидаемыми.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Конструктор исключения.
     * Создает исключение с сообщением "Неверный email или пароль".
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("Не авторизированный пользователь");
    }


}

