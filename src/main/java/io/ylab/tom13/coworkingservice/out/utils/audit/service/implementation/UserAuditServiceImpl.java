package io.ylab.tom13.coworkingservice.out.utils.audit.service.implementation;

import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import io.ylab.tom13.coworkingservice.out.utils.audit.repository.UserAuditRepository;
import io.ylab.tom13.coworkingservice.out.utils.audit.service.UserAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация интерфейса {@link UserAuditService}, предоставляющая методы для логирования действий пользователя.
 *
 * <p>Этот класс использует JWT для получения информации о пользователе из токена авторизации и сохраняет
 * действия пользователя в базу данных, а также логирует их в журнал логирования.</p>
 *
 * <p>Аннотации:</p>
 * <ul>
 *     <li>{@link Service} - обозначает, что этот класс является сервисом Spring.</li>
 *     <li>{@link Slf4j} - генерирует логгер SLF4J для этого класса.</li>
 *     <li>{@link RequiredArgsConstructor} - создает конструктор с требуемыми зависимостями.</li>
 * </ul>
 *
 * @see UserAuditService
 * @see JwtUtil
 * @see UserAuditRepository
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuditServiceImpl implements UserAuditService {

    private final UserAuditRepository userAuditRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void logActionByID(long userId, String actionDescription) {
        userAuditRepository.logToDatabase(userId, actionDescription);
        log.info("Пользователь с ID {} {}.", userId, actionDescription);
    }
}
