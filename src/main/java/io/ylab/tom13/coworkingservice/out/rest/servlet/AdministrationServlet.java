package io.ylab.tom13.coworkingservice.out.rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.services.AdministrationService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.AdministrationServiceImpl;

public class AdministrationServlet extends CoworkingServiceServlet {

    protected final AdministrationService administrationService;

    protected AdministrationServlet() {
        administrationService = new AdministrationServiceImpl();
    }
}
