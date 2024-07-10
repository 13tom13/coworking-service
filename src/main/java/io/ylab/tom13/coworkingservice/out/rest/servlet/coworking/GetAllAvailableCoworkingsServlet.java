package io.ylab.tom13.coworkingservice.out.rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Сервлет для получения всех доступных рабочих пространств.
 */
@WebServlet("/coworking/available")
public class GetAllAvailableCoworkingsServlet extends CoworkingServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected GetAllAvailableCoworkingsServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /**
//     * Обрабатывает HTTP GET запрос для получения всех доступных рабочих пространств.
//     *
//     * @param request  HTTP запрос, может содержать параметры для фильтрации или сортировки
//     * @param response HTTP ответ, который будет содержать список доступных рабочих пространств или ошибку
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasAuthenticated()) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
//            return;
//        }
//        try {
//            Map<String, CoworkingDTO> availableCoworkings = coworkingService.getAllAvailableCoworkings();
//            setJsonResponse(response, availableCoworkings);
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
}