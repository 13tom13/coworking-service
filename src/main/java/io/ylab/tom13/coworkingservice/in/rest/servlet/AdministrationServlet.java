package io.ylab.tom13.coworkingservice.in.rest.servlet;

import io.ylab.tom13.coworkingservice.in.rest.services.AdministrationService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.AdministrationServiceImpl;

public class AdministrationServlet extends CoworkingServiceServlet {

    protected final AdministrationService administrationService;

    protected AdministrationServlet() {
        administrationService = new AdministrationServiceImpl();
    }
}
