package io.ylab.tom13.coworkingservice.out.rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.AdministrationServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет для получения списка всех пользователей администратором.
 */
@WebServlet("/admin/users")
public class GetAllUsersServlet extends AdministrationServlet {

    /**
     * Обрабатывает HTTP GET запрос для получения списка всех пользователей.
     *
     * @param request  HTTP запрос, который может содержать параметры для фильтрации пользователей
     * @param response HTTP ответ, который будет содержать список пользователей в формате JSON
     * @throws IOException если возникает ошибка при чтении или записи данных
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasRole(Role.ADMINISTRATOR)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
            return;
        }

        List<UserDTO> allUsers = administrationService.getAllUsers();
        setJsonResponse(response, allUsers);
    }
}
