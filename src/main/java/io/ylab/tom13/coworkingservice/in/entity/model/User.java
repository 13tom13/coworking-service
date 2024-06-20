package io.ylab.tom13.coworkingservice.in.entity.model;

import lombok.Data;


/**
 * Класс, представляющий сущность пользователя.
 */
@Data
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    private final long id;

    /**
     * Имя пользователя.
     */
    private final String firstName;

    /**
     * Фамилия пользователя.
     */
    private final String lastName;

    /**
     * Email пользователя.
     */
    private final String email;

    /**
     * Пароль пользователя.
     * <p>
     * Обратите внимание, что хранение пароля в текстовом виде не рекомендуется в продакшн-приложениях.
     * Рекомендуется хранить пароль в хэшированном виде с использованием надежного алгоритма хэширования,
     * такого как BCrypt.
     * </p>
     */
    private final String password;
}
