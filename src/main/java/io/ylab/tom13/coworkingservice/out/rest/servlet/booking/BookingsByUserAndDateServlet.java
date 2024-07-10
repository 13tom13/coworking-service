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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервлет для получения списка бронирований по идентификатору пользователя и дате.
 */
@WebServlet("/booking/user/date")
public class BookingsByUserAndDateServlet extends BookingServlet {

    /**
     * Обрабатывает HTTP GET запрос для получения списка бронирований по идентификатору пользователя и дате.
     *
     * @param request  HTTP запрос, содержащий параметры идентификатора пользователя и даты
     * @param response HTTP ответ, который будет содержать список бронирований в формате JSON
     * @throws IOException если возникает ошибка при чтении или записи данных
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
            return;
        }
        String userIdStr = request.getParameter(USER_ID);
        String dateStr = request.getParameter(DATE);
        Optional<Long> userIdOpt = getLongParam(userIdStr);
        if (userIdOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат параметра запроса");
            return;
        }
        LocalDate date = LocalDate.parse(dateStr);
        try {
            List<BookingDTO> bookingsByUserAndDate = bookingService.getBookingsByUserAndDate(userIdOpt.get(), date);
            setJsonResponse(response, bookingsByUserAndDate);
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (BookingNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
