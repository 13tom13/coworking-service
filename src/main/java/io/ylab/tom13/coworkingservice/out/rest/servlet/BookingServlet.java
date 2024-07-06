package io.ylab.tom13.coworkingservice.out.rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.BookingServiceImpl;

public abstract class BookingServlet extends CoworkingServiceServlet {

    protected final BookingService bookingService;

    protected BookingServlet() {
        bookingService = new BookingServiceImpl();
    }
}
