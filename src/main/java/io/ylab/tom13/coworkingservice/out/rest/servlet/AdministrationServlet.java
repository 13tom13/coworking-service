package io.ylab.tom13.coworkingservice.out.rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.services.AdministrationService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.AdministrationServiceImpl;

/**
 * Сервлет администрирования, расширяющий {@link CoworkingServiceServlet}.
 * Содержит зависимость от {@link AdministrationService} для выполнения административных задач.
 */
public class AdministrationServlet extends CoworkingServiceServlet {

    /** Сервис администрирования */
    protected final AdministrationService administrationService;

    /**
     * Конструктор инициализирует объект {@link AdministrationService}.
     */
    protected AdministrationServlet() {
        administrationService = new AdministrationServiceImpl();
    }
}
