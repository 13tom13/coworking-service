package io.ylab.tom13.coworkingservice.out.rest.servlet.booking;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.BookingServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet("/booking/availableslots")
public class GetAvailableSlotsForBookingServlet extends BookingServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
            return;
        }
        String coworkingIdStr = request.getParameter(COWORKING_ID);
        String dateStr = request.getParameter(DATE);
        Optional<Long> coworkingIdOpt = getLongParam(coworkingIdStr);
        if (coworkingIdOpt.isEmpty())  {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат параметра");
            return;
        }
        LocalDate date = LocalDate.parse(dateStr);
        try {
            List<TimeSlot> availableSlots = bookingService.getAvailableSlots(coworkingIdOpt.get(), date);
            setJsonResponse(response, availableSlots);
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
