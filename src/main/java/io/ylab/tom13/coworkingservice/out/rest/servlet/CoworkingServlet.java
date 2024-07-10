package io.ylab.tom13.coworkingservice.out.rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.CoworkingService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.CoworkingServiceImpl;

/**
 * Абстрактный сервлет для управления коворкингами, расширяющий {@link CoworkingServiceServlet}.
 * Содержит зависимость от {@link CoworkingService} для выполнения операций с коворкингами.
 */
public abstract class CoworkingServlet extends CoworkingServiceServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected CoworkingServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /** Сервис коворкингов */
//    protected final CoworkingService coworkingService;
//
//    /**
//     * Конструктор инициализирует объект {@link CoworkingService}.
//     */
//    protected CoworkingServlet() {
//        coworkingService = new CoworkingServiceImpl();
//    }
}