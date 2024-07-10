package io.ylab.tom13.coworkingservice.out.rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
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
 * Сервлет для получения всех рабочих пространств.
 */
@WebServlet("/coworking/all")
public class GetAllCoworkingsServlet extends CoworkingServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected GetAllCoworkingsServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /**
//     * Обрабатывает HTTP GET запрос для получения всех рабочих пространств.
//     *
//     * @param request  HTTP запрос, может содержать параметры для фильтрации или сортировки
//     * @param response HTTP ответ, который будет содержать список всех рабочих пространств или ошибку
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
//            return;
//        }
//        try {
//            Map<String, CoworkingDTO> allCoworking = coworkingService.getAllCoworking();
//            setJsonResponse(response, allCoworking);
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
}
