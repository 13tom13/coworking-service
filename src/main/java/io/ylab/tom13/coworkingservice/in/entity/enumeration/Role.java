package io.ylab.tom13.coworkingservice.in.entity.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Перечисление, представляющее роли пользователей.
 * Каждая роль имеет уникальный идентификатор и отображаемое наименование.
 */
@Getter
@RequiredArgsConstructor
public enum Role {
    /**
     * Роль "Пользователь".
     */
    USER("Пользователь"),

    /**
     * Роль "Модератор".
     */
    MODERATOR("Модератор"),

    /**
     * Роль "Администратор".
     */
    ADMINISTRATOR("Администратор");

    /**
     * Отображаемое наименование роли.
     */
    private final String displayName;
}

