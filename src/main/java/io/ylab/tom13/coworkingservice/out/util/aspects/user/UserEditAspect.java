//package io.ylab.tom13.coworkingservice.out.utils.aspects.user;
//
//import io.ylab.tom13.coworkingservice.out.database.DatabaseConnection;
//import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
//import io.ylab.tom13.coworkingservice.out.utils.Session;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
///**
// * Аспект для логирования редактирования данных пользователя.
// */
//@Aspect
//@Component
//public class UserEditAspect extends UserAspect {
//
//    /**
//     * Конструктор, инициализирующий соединение с базой данных.
//     *
//     * @param databaseConnection
//     */
//    @Autowired
//    protected UserEditAspect(DatabaseConnection databaseConnection) {
//        super(databaseConnection);
//    }
//
//    /**
//     * Точка входа для метода редактирования данных пользователя.
//     * Отслеживает выполнение метода editUser в UserEditService.
//     * Аргумент метода должен быть типа UserDTO.
//     */
//    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.services.UserEditService.editUser(..))")
//    public void editUserPointcut() {
//    }
//
//    /**
//     * После успешного выполнения метода editUser.
//     * Логирует изменение данных пользователем.
//     */
//    @After("editUserPointcut()")
//    public void logUserEdit() {
//        Optional<UserDTO> user = Session.getInstance().getUser();
//        String userEmail = user.isPresent() ? user.get().email() : "anonymous";
//        String action = "изменил свои данные";
//        logger.info("Пользователь с email {} {}.", userEmail, action);
//        logToDatabase(userEmail, action);
//    }
//}
