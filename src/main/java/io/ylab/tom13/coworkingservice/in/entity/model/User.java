package io.ylab.tom13.coworkingservice.in.entity.model;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;

/**
 * Запись о пользователе системы.
 * Этот record содержит уникальный идентификатор пользователя, имя, фамилию,
 * адрес электронной почты, пароль и роль пользователя.
 */
public record User(
        /**
         * Уникальный идентификатор пользователя.
         */
        long id,

        /**
         * Имя пользователя.
         */
        String firstName,

        /**
         * Фамилия пользователя.
         */
        String lastName,

        /**
         * Адрес электронной почты пользователя.
         */
        String email,

        /**
         * Пароль пользователя.
         */
        String password,

        /**
         * Роль пользователя в системе.
         */
        Role role
) {
}

