package io.ylab.tom13.coworkingservice.out.rest.servlet.booking;

import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.BookingServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/booking")
public class BookingsByIdServlet extends BookingServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
            return;
        }
        String bookingIdStr = request.getParameter(BOOKING_ID);
        Optional<Long> bookingIdOpt = getLongParam(bookingIdStr);
        if (bookingIdOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат параметра запроса");
            return;
        }
        try {
            BookingDTO bookingById = bookingService.getBookingById(bookingIdOpt.get());
            setJsonResponse(response, bookingById);
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (BookingNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
