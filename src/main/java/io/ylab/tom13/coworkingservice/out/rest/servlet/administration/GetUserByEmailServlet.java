package io.ylab.tom13.coworkingservice.out.rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.servlet.AdministrationServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет для получения информации о пользователе по электронной почте администратором.
 */
@WebServlet("/admin/user")
public class GetUserByEmailServlet extends AdministrationServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected GetUserByEmailServlet(UserRepository userRepository) {
        super(userRepository);
    }
//
//    /**
//     * Обрабатывает HTTP GET запрос для получения информации о пользователе по электронной почте.
//     *
//     * @param request  HTTP запрос, содержащий параметры для получения информации о пользователе
//     * @param response HTTP ответ, который будет содержать информацию о пользователе в формате JSON
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasRole(Role.ADMINISTRATOR)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
//            return;
//        }
//
//        String email = request.getParameter(EMAIL);
//        if (email == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Параметр '%s' пуст или отсутствует", EMAIL));
//            return;
//        }
//
//        try {
//            UserDTO user = administrationService.getUserByEmail(email);
//            setJsonResponse(response, user);
//
//        } catch (UserNotFoundException e) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        }
//    }
}
