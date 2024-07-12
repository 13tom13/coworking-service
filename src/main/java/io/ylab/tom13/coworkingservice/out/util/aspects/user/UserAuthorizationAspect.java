package io.ylab.tom13.coworkingservice.out.util.aspects.user;

import io.ylab.tom13.coworkingservice.out.database.DatabaseConnection;
import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования авторизации пользователя.
 */
@Aspect
@Component
public class UserAuthorizationAspect extends UserAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthorizationAspect.class);

    @Autowired
    protected UserAuthorizationAspect(DatabaseConnection databaseConnection) {
        super(databaseConnection);
    }

    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService.login(..)) && args(authorizationDTO)")
    public void loginPointcut(AuthorizationDTO authorizationDTO) {
    }

    @AfterReturning(pointcut = "loginPointcut(authorizationDTO)", returning = "userDTO", argNames = "authorizationDTO,userDTO")
    public void afterLoginUser(AuthorizationDTO authorizationDTO, UserDTO userDTO) {
        if (userDTO != null) {
            String email = userDTO.email();
            String action = "авторизован";
            logToDatabase(email, action);
            logger.info("Пользователь с email {} {}.", email, action);
        }
    }
}