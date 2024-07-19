package io.ylab.tom13.coworkingservice.out.utils.audit.aspect;

import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import io.ylab.tom13.coworkingservice.out.utils.audit.service.UserAuditService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Аспект для логирования действий пользователя в контроллерах бронирования.
 * Реализует функциональность логирования различных действий пользователей, связанных с бронированием.
 */
@Aspect
@Component
@Slf4j
public class UserBookingActionAspect extends UserAuditAspect {

    public UserBookingActionAspect(UserAuditService userAuditService, JwtUtil jwtUtil) {
        super(userAuditService, jwtUtil);
    }

    /**
     * Точка присоединения для логирования действий пользователя в контроллерах бронирования.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.BookingControllerSpring+.*(..))")
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
        Optional<Long> userIdOpt = getUserIdFromRequest();
        userIdOpt.ifPresentOrElse(
                userId -> userAuditService.logActionByID(userId, determineAction(methodName)),
                () -> log.warn(TOKEN_WARNING)
        );
    }

    /**
     * Определяет описание действия пользователя на основе имени метода контроллера бронирования.
     *
     * @param methodName имя метода контроллера бронирования
     * @return описание действия пользователя
     */
    @Override
    protected String determineAction(String methodName) {
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