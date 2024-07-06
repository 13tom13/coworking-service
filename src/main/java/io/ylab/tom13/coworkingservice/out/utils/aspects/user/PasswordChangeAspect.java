package io.ylab.tom13.coworkingservice.out.utils.aspects.user;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Optional;

@Aspect
public class PasswordChangeAspect extends UserAspect {


    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.services.UserEditService.editPassword(..))")
    public void editPasswordPointcut() {
    }

    @After("editPasswordPointcut()")
    public void logPasswordChange() {
        Optional<UserDTO> user = Session.getInstance().getUser();
        String userEmail = user.isPresent() ? user.get().email() : "anonymous";
        String action = "изменил пароль";
        logger.info("Пользователь с email {} {}.", userEmail, action);
        logToDatabase(userEmail, action);
    }
}
