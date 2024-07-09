package io.ylab.tom13.coworkingservice.out.utils.aspects.user;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Optional;

/**
 * Аспект для логирования выхода пользователя из системы.
 */
@Aspect
public class UserLogoutAspect extends UserAspect {

    /**
     * Точка входа для метода удаления пользователя из сессии.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.utils.Session.removeUser(..))")
    public void removeUserPointcut() {
    }

    /**
     * Перед выполнением метода удаления пользователя.
     * Логирует выход пользователя из системы.
     */
    @Before("removeUserPointcut()")
    public void afterRemoveUser() {
        Optional<UserDTO> user = Session.getInstance().getUser();
        if (user.isPresent()) {
            String email = user.get().email();
            String action = "вышел из системы";
            logToDatabase(email, action);
            logger.info("Пользователь с email {} {}.", email, action);
        } else {
            logger.warn("Попытка выхода из системы неавторизованным пользователем.");
        }
    }
}