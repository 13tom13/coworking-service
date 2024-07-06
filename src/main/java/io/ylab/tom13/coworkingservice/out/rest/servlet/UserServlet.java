package io.ylab.tom13.coworkingservice.out.rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.UserEditServiceImpl;

public abstract class UserServlet extends CoworkingServiceServlet {

    protected final UserEditService userEditService;

    protected UserServlet() {
        userEditService = new UserEditServiceImpl();
    }
}
