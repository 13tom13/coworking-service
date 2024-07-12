package io.ylab.tom13.coworkingservice.out.util.aspects.user;


import io.ylab.tom13.coworkingservice.out.database.DatabaseConnection;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования действий пользователей в системе бронирования.
 * Логирует операции создания, изменения и удаления бронирований, а также получение списков бронирований.
 */
@Aspect
@Component
public class UserActionLoggingAspect extends UserAspect {

    /**
     * Конструктор, инициализирующий соединение с базой данных.
     *
     * @param databaseConnection
     */
    @Autowired
    protected UserActionLoggingAspect(DatabaseConnection databaseConnection) {
        super(databaseConnection);
    }

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
//        Optional<UserDTO> user = Session.getInstance().getUser();
//        if (user.isPresent() && user.get().role() == Role.USER) {
//            String userEmail = user.get().email();
//
//            String methodName = joinPoint.getSignature().getName();
//            String action = determineAction(methodName);
//
//            logger.info("Пользователь с email {} {} ", userEmail, action);
//            logToDatabase(userEmail, action);
//        }
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
