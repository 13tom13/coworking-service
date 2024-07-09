package io.ylab.tom13.coworkingservice.out.utils.aspects.user;


import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Optional;

/**
 * Аспект для логирования действий пользователей в системе бронирования.
 * Логирует операции создания, изменения и удаления бронирований, а также получение списков бронирований.
 */
@Aspect
public class UserActionLoggingAspect extends UserAspect {

    /**
     * Точка входа для методов репозитория бронирований.
     * Отслеживает выполнение всех методов в классе BookingRepository.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.repositories.BookingRepository.*(..))")
    public void bookingRepositoryMethods() {
    }

    /**
     * После успешного выполнения методов репозитория бронирований.
     * Логирует действия пользователей, если пользователь имеет роль USER.
     * @param joinPoint точка присоединения, содержащая информацию о вызываемом методе
     * @param result результат возвращаемый методом
     */
    @AfterReturning(pointcut = "bookingRepositoryMethods()", returning = "result")
    public void logUserAction(JoinPoint joinPoint, Object result) {
        Optional<UserDTO> user = Session.getInstance().getUser();
        System.out.println("тест изменений");
        if (user.isPresent() && user.get().role() == Role.USER) {
            String userEmail = user.get().email();

            String methodName = joinPoint.getSignature().getName();
            String action = determineAction(methodName);

            logger.info("Пользователь с email {} {} ", userEmail, action);
            System.out.println("test тест");
            logToDatabase(userEmail, action);
        }
    }

    /**
     * Определяет текстовое описание действия на основе имени вызываемого метода.
     * @param methodName имя вызываемого метода
     * @return текстовое описание действия
     */
    private String determineAction(String methodName) {
        return switch (methodName) {
            case "createBooking" -> "создал бронирование";
            case "updateBooking" -> "изменил бронирование";
            case "deleteBooking" -> "удалил бронирование";
            case "getBookingById" -> "получил бронирование";
            case "getBookingsByUser" -> "Получил список бронирований";
            case "getBookingsByUserAndDate" -> "Получил список бронирований по дате";
            case "getBookingsByUserAndCoworking" -> "Получил список бронирований по коворкингу";
            case "getBookingsByCoworkingAndDate" -> "Получил список бронирований по коворкингу и дате";
            default -> "выполнил действие с бронированием";
        };
    }
}
