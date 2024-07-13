package io.ylab.tom13.coworkingservice.out.utils.aspects;

import io.ylab.tom13.coworkingservice.out.database.DatabaseConnection;
import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования действий пользователя в системе.
 * Реализует функциональность логирования различных действий пользователей, таких как редактирование данных,
 * изменение пароля, бронирование, авторизация и выход из системы.
 */
@Aspect
@Component
public class UserActionLoggingAspect extends UserAspect {

    /**
     * Конструктор, инъектирующий зависимости для работы с базой данных и JWT утилитой.
     *
     * @param databaseConnection соединение с базой данных
     * @param jwtUtil            утилита для работы с JWT
     */
    public UserActionLoggingAspect(DatabaseConnection databaseConnection, JwtUtil jwtUtil) {
        super(databaseConnection, jwtUtil);
    }

    /**
     * Точка присоединения для логирования редактирования данных пользователем.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.UserEditController.editUser(..))")
    public void editUserPointcut() {
    }

    /**
     * Аспект после успешного возврата из метода редактирования данных.
     *
     * @param joinPoint точка присоединения
     * @param result    результат выполнения метода
     */
    @AfterReturning(pointcut = "editUserPointcut()", returning = "result")
    public void logUserEdit(JoinPoint joinPoint, Object result) {
        logAction(joinPoint, "изменил свои данные");
    }

    /**
     * Точка присоединения для логирования изменения пароля пользователем.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.UserEditController.editPassword(..))")
    public void editPasswordPointcut() {
    }

    /**
     * Аспект после успешного возврата из метода изменения пароля.
     *
     * @param joinPoint точка присоединения
     * @param result    результат выполнения метода
     */
    @AfterReturning(pointcut = "editPasswordPointcut()", returning = "result")
    public void logEditPassword(JoinPoint joinPoint, Object result) {
        logAction(joinPoint, "изменил пароль");
    }


    /**
     * Точка присоединения для логирования успешной авторизации пользователя.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.AuthorizationController+.login(..))")
    public void loginPointcut() {
    }

    /**
     * Аспект после успешной авторизации пользователя.
     *
     * @param joinPoint      точка присоединения
     * @param responseEntity ответ от метода авторизации
     */
    @AfterReturning(pointcut = "loginPointcut()", returning = "responseEntity")
    public void afterLoginUser(JoinPoint joinPoint, ResponseEntity<?> responseEntity) {
        if (responseEntity != null && responseEntity.getBody() instanceof String token) {
            Long userId = jwtUtil.getIdFromToken(token);
            String action = "авторизован";
            logToDatabase(userId, action);
            logger.info("Пользователь с ID {} {}.", userId, action);
        } else {
            logger.warn("Не удалось извлечь токен из тела ответа ResponseEntity.");
        }
    }

    /**
     * Точка присоединения для логирования выхода пользователя из системы.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.AuthorizationController.logout(..))")
    public void logoutPointcut() {
    }

    /**
     * Аспект после успешного выхода пользователя из системы.
     *
     * @param joinPoint точка присоединения
     * @param result    результат выполнения метода
     */
    @AfterReturning(pointcut = "logoutPointcut()", returning = "result")
    public void logoutUser(JoinPoint joinPoint, Object result) {
        logAction(joinPoint, "вышел из системы");
    }

}
