package io.ylab.tom13.coworkingservice.in.rest.servlet.booking;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.servlet.BookingServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static io.ylab.tom13.coworkingservice.in.utils.security.SecurityController.hasAuthenticated;

@WebServlet("/booking/availableslots")
public class GetAvailableSlotsForBookingServlet extends BookingServlet {

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
            setJsonResponse(response,availableSlots);
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
