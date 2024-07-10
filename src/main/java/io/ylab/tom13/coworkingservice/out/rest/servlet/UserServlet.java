package io.ylab.tom13.coworkingservice.out.rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.UserEditServiceImpl;

/**
 * Абстрактный сервлет для управления пользователями, расширяющий {@link CoworkingServiceServlet}.
 * Содержит зависимость от {@link UserEditService} для выполнения операций с пользователями.
 */
public abstract class UserServlet extends CoworkingServiceServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected UserServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /** Сервис редактирования пользователей */
//    protected final UserEditService userEditService;
//
//    /**
//     * Конструктор инициализирует объект {@link UserEditService}.
//     */
//    protected UserServlet() {
//        userEditService = new UserEditServiceImpl();
//    }
}
