package io.ylab.tom13.coworkingservice.out.utils.audit.service;

/**
 * Интерфейс UserAuditService предоставляет методы для логирования действий пользователя.
 * Он используется для записи информации о действиях пользователя в базу данных и журнал логирования.
 *
 * @see org.aspectj.lang.JoinPoint
 */
public interface UserAuditService {

    /**
     * Логирует действие пользователя в базу данных и в журнал логирования на основе текущего HTTP-запроса.
     *
     * @param actionDescription описание действия пользователя
     */
    void logActionByID(long userId, String actionDescription);
}
