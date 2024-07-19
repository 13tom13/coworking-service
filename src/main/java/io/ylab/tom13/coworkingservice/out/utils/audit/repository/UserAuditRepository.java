package io.ylab.tom13.coworkingservice.out.utils.audit.repository;

/**
 * Интерфейс UserAuditRepository предоставляет методы для записи действий пользователя в базу данных.
 *
 * <p>Этот интерфейс определяет метод для логирования действий пользователя, который должен быть реализован
 * конкретным классом репозитория для сохранения данных в базу данных.</p>
 */
public interface UserAuditRepository {

    /**
     * Записывает действие пользователя в базу данных.
     *
     * @param id     идентификатор пользователя
     * @param action описание действия пользователя
     */
    void logToDatabase(long id, String action);
}
