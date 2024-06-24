package io.ylab.tom13.coworkingservice.in.entity.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    USER("Пользователь"),
    MODERATOR("Модератор"),
    ADMINISTRATOR("Администратор");

    @Getter
    private final String displayName;
}

