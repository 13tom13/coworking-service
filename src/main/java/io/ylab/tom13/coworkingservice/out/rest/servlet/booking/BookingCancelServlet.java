//package io.ylab.tom13.coworkingservice.out.rest.servlet.booking;
//
//import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingNotFoundException;
//import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
//import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
//import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
//import io.ylab.tom13.coworkingservice.out.rest.servlet.BookingServlet;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.util.Optional;
//
//
///**
// * Сервлет для отмены бронирования.
// */
//@WebServlet("/booking/cancel")
//public class BookingCancelServlet extends BookingServlet {
//    /**
//     * Конструктор инициализирует экземпляры UserRepository и Session.
//     *
//     * @param userRepository
//     */
//    protected BookingCancelServlet(UserRepository userRepository) {
//        super(userRepository);
//    }
//
//    /**
//     * Сообщение об успешной отмене бронирования.
//     */
//    public static final String RESPONSE_SUCCESS = "Бронирование успешно отменено";
//
//    /**
//     * Обрабатывает HTTP DELETE запрос для отмены бронирования.
//     *
//     * @param request  HTTP запрос, содержащий идентификатор бронирования в параметрах запроса
//     * @param response HTTP ответ, который будет содержать результат отмены бронирования в формате JSON
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasAuthenticated()) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
//            return;
//        }
//
//        String bookingIdStr = request.getParameter(BOOKING_ID);
//        Optional<Long> bookingIdOpt = getLongParam(bookingIdStr);
//        if (bookingIdOpt.isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат параметра запроса");
//            return;
//        }
//        try {
//            bookingService.cancelBooking(bookingIdOpt.get());
//            setJsonResponse(response, RESPONSE_SUCCESS);
//        } catch (BookingNotFoundException e) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//}