//package io.ylab.tom13.coworkingservice.out.rest.servlet.booking;
//
//import io.ylab.tom13.coworkingservice.out.entity.dto.BookingDTO;
//import io.ylab.tom13.coworkingservice.out.exceptions.booking.BookingConflictException;
//import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
//import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
//import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
//import io.ylab.tom13.coworkingservice.out.rest.servlet.BookingServlet;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
///**
// * Сервлет для создания нового бронирования.
// */
//@WebServlet("/booking/create")
//public class BookingCreateServlet extends BookingServlet {
//    /**
//     * Конструктор инициализирует экземпляры UserRepository и Session.
//     *
//     * @param userRepository
//     */
//    protected BookingCreateServlet(UserRepository userRepository) {
//        super(userRepository);
//    }
//
//    /**
//     * Обрабатывает HTTP POST запрос для создания нового бронирования.
//     *
//     * @param request  HTTP запрос, содержащий данные о бронировании в формате JSON
//     * @param response HTTP ответ, который будет содержать результат создания бронирования в формате JSON
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasAuthenticated()) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
//            return;
//        }
//        String jsonRequest = getJsonRequest(request);
//        BookingDTO bookingDTO = objectMapper.readValue(jsonRequest, BookingDTO.class);
//        try {
//            BookingDTO booking = bookingService.createBooking(bookingDTO);
//            setJsonResponse(response, booking, HttpServletResponse.SC_CREATED);
//        } catch (BookingConflictException e) {
//            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//}