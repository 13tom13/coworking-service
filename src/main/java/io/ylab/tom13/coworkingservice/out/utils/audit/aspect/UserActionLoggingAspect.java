package io.ylab.tom13.coworkingservice.out.utils.audit.aspect;

import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import io.ylab.tom13.coworkingservice.out.utils.audit.service.UserAuditService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Аспект для логирования действий пользователя в системе.
 * Реализует функциональность логирования различных действий пользователей, таких как редактирование данных,
 * изменение пароля, бронирование, авторизация и выход из системы.
 */
@Aspect
@Component
@Slf4j
public class UserActionLoggingAspect extends UserAuditAspect {

    private static final String LOGIN_METHOD = "login";

    public UserActionLoggingAspect(UserAuditService userAuditService, JwtUtil jwtUtil) {
        super(userAuditService, jwtUtil);
    }

    /**
     * Точка присоединения для логирования действий пользователя в контроллерах редактирования и авторизации.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.UserEditControllerSpring+.*(..)) || " +
              "execution(* io.ylab.tom13.coworkingservice.out.rest.controller.AuthorizationControllerSpring+.*(..))")
    public void userActionsPointcut() {
    }

    /**
     * Аспект после успешного возврата из метода контроллеров редактирования и авторизации.
     *
     * @param joinPoint точка присоединения
     * @param result    результат выполнения метода
     */
    @AfterReturning(pointcut = "userActionsPointcut()", returning = "result")
    public void logUserAction(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        Optional<Long> userIdOpt;
        if (LOGIN_METHOD.equals(methodName) && result instanceof ResponseEntity<?>) {
            userIdOpt = getUserIdFromResponseEntity((ResponseEntity<?>) result);
        } else {
            userIdOpt = getUserIdFromRequest();
        }
        userIdOpt.ifPresentOrElse(
                userId -> userAuditService.logActionByID(userId, determineAction(methodName)),
                () -> log.warn(TOKEN_WARNING)
        );
    }

    /**
     * Определяет описание действия пользователя на основе имени метода контроллера.
     *
     * @param methodName имя метода контроллера
     * @return описание действия пользователя
     */
    @Override
    protected String determineAction(String methodName) {
        return switch (methodName) {
            case "editUser" -> "изменил свои данные";
            case "editPassword" -> "изменил пароль";
            case "login" -> "авторизован";
            case "logout" -> "вышел из системы";
            default -> "выполнил действие";
        };
    }

}
