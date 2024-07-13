package io.ylab.tom13.coworkingservice.out.utils.aspects;

import io.ylab.tom13.coworkingservice.out.database.DatabaseConnection;
import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования действий пользователя в контроллерах бронирования.
 * Реализует функциональность логирования различных действий пользователей, связанных с бронированием.
 */
@Aspect
@Component
public class UserBookingActionAspect extends UserAspect {

    /**
     * Конструктор, инъектирующий зависимости для работы с базой данных и JWT утилитой.
     *
     * @param databaseConnection соединение с базой данных
     * @param jwtUtil            утилита для работы с JWT
     */
    public UserBookingActionAspect(DatabaseConnection databaseConnection, JwtUtil jwtUtil) {
        super(databaseConnection, jwtUtil);
    }

    /**
     * Точка присоединения для логирования действий пользователя в контроллерах бронирования.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.BookingController+.*(..))")
    public void bookingUserPointcut() {
    }

    /**
     * Аспект после успешного возврата из метода бронирования.
     *
     * @param joinPoint точка присоединения
     * @param result    результат выполнения метода
     */
    @AfterReturning(pointcut = "bookingUserPointcut()", returning = "result")
    public void logUserBookingAction(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logAction(joinPoint, determineAction(methodName));
    }

    /**
     * Определяет описание действия пользователя на основе имени метода контроллера бронирования.
     *
     * @param methodName имя метода контроллера бронирования
     * @return описание действия пользователя
     */
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