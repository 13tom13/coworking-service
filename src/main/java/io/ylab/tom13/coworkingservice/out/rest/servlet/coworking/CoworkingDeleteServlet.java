package io.ylab.tom13.coworkingservice.out.rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет для удаления рабочего пространства (coworking space).
 */
@WebServlet("/coworking/delete")
public class CoworkingDeleteServlet extends CoworkingServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected CoworkingDeleteServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /**
//     * Обрабатывает HTTP DELETE запрос для удаления рабочего пространства.
//     *
//     * @param request  HTTP запрос, содержащий параметр 'coworkingId', идентификатор рабочего пространства
//     * @param response HTTP ответ, который будет содержать сообщение об успешном удалении или ошибку
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
//            return;
//        }
//
//        String coworkingIdStr = request.getParameter(COWORKING_ID);
//        if (coworkingIdStr == null || coworkingIdStr.isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Параметр 'coworkingId' пуст или отсутствует");
//            return;
//        }
//
//        long coworkingId;
//        try {
//            coworkingId = Long.parseLong(coworkingIdStr);
//        } catch (NumberFormatException e) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный 'coworkingId' параметр");
//            return;
//        }
//
//        try {
//            coworkingService.deleteCoworking(coworkingId);
//            String responseSuccess = "Коворкинг успешно удален";
//            setJsonResponse(response, responseSuccess);
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        } catch (CoworkingNotFoundException e) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        }
//    }
}