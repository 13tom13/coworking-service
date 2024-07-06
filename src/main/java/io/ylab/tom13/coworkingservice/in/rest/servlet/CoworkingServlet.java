package io.ylab.tom13.coworkingservice.in.rest.servlet;

import io.ylab.tom13.coworkingservice.in.rest.services.CoworkingService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.CoworkingServiceImpl;

public abstract class CoworkingServlet extends CoworkingServiceServlet {

    protected final CoworkingService coworkingService;

    protected CoworkingServlet() {
        coworkingService = new CoworkingServiceImpl();
    }
}
