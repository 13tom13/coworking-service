package io.ylab.tom13.coworkingservice.in.rest.servlet.booking;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.BookingServiceImpl;
import io.ylab.tom13.coworkingservice.in.rest.servlet.CoworkingServiceServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static io.ylab.tom13.coworkingservice.in.utils.security.SecurityController.hasAuthenticated;

@WebServlet("/booking/availableslots")
public class GetAvailableSlotsForBookingServlet extends CoworkingServiceServlet {

    private final BookingService bookingService;

    public GetAvailableSlotsForBookingServlet() {
        this.bookingService = new BookingServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
            return;
        }
        String coworkingIdStr = request.getParameter("coworkingId");
        String dateStr = request.getParameter("date");
        long coworkingId = Long.parseLong(coworkingIdStr);
        LocalDate date = LocalDate.parse(dateStr);
        try {
            List<TimeSlot> availableSlots = bookingService.getAvailableSlots(coworkingId, date);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.format("Свободные слоты для бронирования в коворкинге с id %s на дату %s",
                    coworkingId, date));
            response.getWriter().write(objectMapper.writeValueAsString(availableSlots));
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
