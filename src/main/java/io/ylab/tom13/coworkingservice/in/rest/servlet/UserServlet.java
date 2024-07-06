package io.ylab.tom13.coworkingservice.in.rest.servlet;

import io.ylab.tom13.coworkingservice.in.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.UserEditServiceImpl;

public abstract class UserServlet extends CoworkingServiceServlet {

    protected final UserEditService userEditService;

    protected UserServlet() {
        userEditService = new UserEditServiceImpl();
    }
}
