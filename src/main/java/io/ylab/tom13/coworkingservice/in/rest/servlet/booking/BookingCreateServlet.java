package io.ylab.tom13.coworkingservice.in.rest.servlet.booking;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.BookingServiceImpl;
import io.ylab.tom13.coworkingservice.in.rest.servlet.CoworkingServiceServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.in.utils.security.SecurityController.hasAuthenticated;

@WebServlet("/booking/create")
public class BookingCreateServlet extends CoworkingServiceServlet {

    private final BookingService bookingService;

    public BookingCreateServlet() {
        this.bookingService = new BookingServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
            return;
        }
        String jsonRequest = getJsonRequest(request);
        BookingDTO bookingDTO = objectMapper.readValue(jsonRequest, BookingDTO.class);
        try {
            BookingDTO booking = bookingService.createBooking(bookingDTO);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString("Бронирование успешно создана: "));
            response.getWriter().write(objectMapper.writeValueAsString(booking));
        } catch (BookingConflictException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
