package io.ylab.tom13.coworkingservice.out.rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.AdministrationService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.AdministrationServiceImpl;

/**
 * Сервлет администрирования, расширяющий {@link CoworkingServiceServlet}.
 * Содержит зависимость от {@link AdministrationService} для выполнения административных задач.
 */
public class AdministrationServlet extends CoworkingServiceServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected AdministrationServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /** Сервис администрирования */
//    protected final AdministrationService administrationService;
//
//    /**
//     * Конструктор инициализирует объект {@link AdministrationService}.
//     */
//    protected AdministrationServlet() {
//        administrationService = new AdministrationServiceImpl();
//    }
}
