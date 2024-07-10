package io.ylab.tom13.coworkingservice.out.utils.aspects.user;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Аспект для логирования авторизации пользователя.
 */
@Aspect
public class UserAuthorizationAspect extends UserAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthorizationAspect.class);

    /**
     * Точка входа для метода установки пользователя в сессию.
     * Отслеживает выполнение метода setUser в классе Session.
     * Аргумент метода должен быть типа UserDTO.
     * @param user пользователь, который будет установлен в сессию
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.utils.Session.setUser(..)) && args(user)")
    public void setUserPointcut(UserDTO user) {
    }

    /**
     * После успешного выполнения метода setUser.
     * Логирует авторизацию пользователя.
     * @param user авторизованный пользователь
     */
    @After(value = "setUserPointcut(user)", argNames = "user")
    public void afterSetUser(UserDTO user) {
        String email = user.email();
        String action = "авторизован";
        logToDatabase(email, action);
        logger.info("Пользователь с email {} {}.", email, action);
    }
}