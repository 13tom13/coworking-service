package io.ylab.tom13.coworkingservice.in.rest.servlet.booking;

import io.ylab.tom13.coworkingservice.in.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.BookingServiceImpl;
import io.ylab.tom13.coworkingservice.in.rest.servlet.CoworkingServiceServlet;

public abstract class BookingServlet extends CoworkingServiceServlet {

    protected final BookingService bookingService;

    protected BookingServlet() {
        bookingService = new BookingServiceImpl();
    }
}
