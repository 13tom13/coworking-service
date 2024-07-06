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

import static io.ylab.tom13.coworkingservice.out.utils.security.SecurityController.hasAuthenticated;

@WebServlet("/booking")
public class BookingsByIdServlet extends BookingServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
            return;
        }
        String bookingIdStr = request.getParameter("bookingId");
        long bookingId = Long.parseLong(bookingIdStr);
        try {
            BookingDTO bookingById = bookingService.getBookingById(bookingId);
            setJsonResponse(response, bookingById);
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (BookingNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
