package io.ylab.tom13.coworkingservice.out.utils.aspects;

import io.ylab.tom13.coworkingservice.out.database.DatabaseConnection;
import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования действий пользователя.
 */
@Aspect
@Component
public class UserActionLoggingAspect extends UserAspect {

    @Autowired
    public UserActionLoggingAspect(DatabaseConnection databaseConnection, JwtUtil jwtUtil) {
        super(databaseConnection, jwtUtil);
    }


    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.UserEditController.editUser(..))")
    public void editUserPointcut() {
    }

    @AfterReturning(pointcut = "editUserPointcut()", returning = "result")
    public void logUserEdit(JoinPoint joinPoint, Object result) {
        logAction(joinPoint, "изменил свои данные");
    }


    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.UserEditController.editPassword(..))")
    public void editPasswordPointcut() {
    }

    @AfterReturning(pointcut = "editPasswordPointcut()", returning = "result")
    public void logEditPassword(JoinPoint joinPoint, Object result) {
        logAction(joinPoint, "изменил пароль");
    }


    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.BookingController+.*(..))")
    public void bookingUserPointcut() {
    }

    @AfterReturning(pointcut = "bookingUserPointcut()", returning = "result")
    public void logUserBookingAction(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logAction(joinPoint, determineAction(methodName));
    }


    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.AuthorizationController+.login(..))")
    public void loginPointcut() {
    }

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

    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.AuthorizationController.logout(..))")
    public void logoutPointcut() {
    }

    @AfterReturning(pointcut = "logoutPointcut()", returning = "result")
    public void logoutUser(JoinPoint joinPoint, Object result) {
        logAction(joinPoint, "вышел из системы");
    }


    private String determineAction(String methodName) {
        return switch (methodName) {
            case "createBooking" -> "создал бронирование";
            case "updateBooking" -> "изменил бронирование";
            case "deleteBooking" -> "удалил бронирование";
            case "getBookingById" -> "получил бронирование";
            case "getBookingsByUser" -> "получил список бронирований";
            case "getBookingsByUserAndDate" -> "получил список бронирований по дате";
            case "getBookingsByUserAndCoworking" -> "получил список бронирований по коворкингу";
            case "getBookingsByCoworkingAndDate" -> "получил список бронирований по коворкингу и дате";
            default -> "выполнил действие с бронированием";
        };
    }
}
