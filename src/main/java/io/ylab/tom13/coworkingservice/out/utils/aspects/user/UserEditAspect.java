package io.ylab.tom13.coworkingservice.out.utils.aspects.user;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Optional;

@Aspect
public class UserEditAspect extends UserAspect {


    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.services.UserEditService.editUser(..)) && args(userDTO)")
    public void editUserPointcut() {
    }

    @AfterReturning(pointcut = "editUserPointcut()", returning = "result")
    public void logUserEdit(JoinPoint joinPoint, Object result) {
        Optional<UserDTO> user = Session.getInstance().getUser();
        String userEmail = user.isPresent() ? user.get().email() : "anonymous";
        String action = "изменил свои данные";
        logger.info("Пользователь с email {} {}.", userEmail, action);
        logToDatabase(userEmail, action);
    }
}
