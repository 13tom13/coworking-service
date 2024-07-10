package io.ylab.tom13.coworkingservice.out.utils;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Класс для управления атрибутами сеанса.
 * Реализует шаблон Singleton для обеспечения единственного экземпляра сеанса в приложении.
 */
public final class Session {

    private final Map<String, Object> attributes;

    private static Session instance;

    @Setter
    private UserDTO user;

    /**
     * Приватный конструктор для инициализации атрибутов сеанса.
     * Инициализирует пустую коллекцию для хранения атрибутов.
     */
    private Session() {
        attributes = new HashMap<>();
    }

    public Optional<UserDTO> getUser() {
        return Optional.ofNullable(user);
    }

    /**
     * Получение единственного экземпляра сеанса (Singleton).
     *
     * @return Единственный экземпляр класса Session.
     */
    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Установка атрибута сеанса по ключу.
     *
     * @param key   Ключ атрибута.
     * @param value Значение атрибута.
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * Получение значения атрибута сеанса по ключу.
     *
     * @param key Ключ атрибута.
     * @return Значение атрибута или null, если атрибут не найден.
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * Удаление атрибута сеанса по ключу.
     *
     * @param key Ключ атрибута для удаления.
     */
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    public void removeUser() {
        this.user = null;
    }
}

