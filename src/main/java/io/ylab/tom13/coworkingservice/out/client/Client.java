package io.ylab.tom13.coworkingservice.out.client;

import io.ylab.tom13.coworkingservice.out.utils.Session;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Базовый абстрактный класс для клиентских компонентов.
 * Предоставляет доступ к сессии приложения.
 */
@Getter
public abstract class Client {

    /**
     * Локальная сессия приложения, используемая клиентским компонентом.
     * -- GETTER --
     *  Получает текущую локальную сессию приложения.
     *
     * @return экземпляр сессии приложения

     */
    protected final Session localSession = Session.getInstance();



}

