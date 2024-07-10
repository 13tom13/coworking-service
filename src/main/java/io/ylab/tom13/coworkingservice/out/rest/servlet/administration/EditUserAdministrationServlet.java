package io.ylab.tom13.coworkingservice.out.rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.AdministrationServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет для редактирования информации о пользователе администратором.
 */
@WebServlet("/admin/user/edit")
public class EditUserAdministrationServlet extends AdministrationServlet {

    /**
     * Обрабатывает HTTP POST запрос для редактирования информации о пользователе.
     *
     * @param request  HTTP запрос, содержащий информацию о пользователе в формате JSON
     * @param response HTTP ответ, который будет отправлен клиенту
     * @throws IOException если возникает ошибка при чтении или записи данных
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasRole(Role.ADMINISTRATOR)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
            return;
        }
        String jsonRequest = getJsonRequest(request);
        UserDTO userFromRequest = objectMapper.readValue(jsonRequest, UserDTO.class);

        try {
            UserDTO user = administrationService.editUserByAdministrator(userFromRequest);
            String responseSuccess = String.format("Пользователь с именем: %s %s и email: %s успешно изменен",
                    user.firstName(), user.lastName(), user.email());
            setJsonResponse(response, responseSuccess);

        } catch (UserAlreadyExistsException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (UserNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
