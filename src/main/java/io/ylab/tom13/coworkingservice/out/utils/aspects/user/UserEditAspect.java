package io.ylab.tom13.coworkingservice.out.utils.aspects.user;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Optional;

@Aspect
public class UserEditAspect extends UserAspect {


    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.services.UserEditService.editUser(..)) && args(userDTO)")
    public void editUserPointcut(UserDTO userDTO) {
    }

    @Around(value = "editUserPointcut(userDTO)", argNames = "joinPoint,userDTO")
    public UserDTO logUserEdit(ProceedingJoinPoint joinPoint, UserDTO userDTO) throws Throwable {
        Optional<UserDTO> user = Session.getInstance().getUser();
        String userEmail = user.isPresent() ? user.get().email() : "anonymous";
        String action = "изменил свои данные";
        Object result = joinPoint.proceed();
        logger.info("Пользователь с email {} {}.", userEmail, action);
        logToDatabase(userEmail, action);
        return (UserDTO) result;
    }
}
