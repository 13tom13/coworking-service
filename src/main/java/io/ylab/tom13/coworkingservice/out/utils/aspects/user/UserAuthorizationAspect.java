package io.ylab.tom13.coworkingservice.out.utils.aspects.user;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class UserAuthorizationAspect extends UserAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthorizationAspect.class);

    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.utils.Session.setUser(..)) && args(user)")
    public void setUserPointcut(UserDTO user) {
    }

    @After(value = "setUserPointcut(user)", argNames = "user")
    public void afterSetUser(UserDTO user) {
        String email = user.email();
        String action = "авторизован";
        logToDatabase(email, action);
        logger.info("Пользователь с email {} {}.", email, action);

    }
}