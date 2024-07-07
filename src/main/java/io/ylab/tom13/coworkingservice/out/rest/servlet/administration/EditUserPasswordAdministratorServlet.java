package io.ylab.tom13.coworkingservice.out.rest.servlet.administration;

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
import java.util.Optional;

import static io.ylab.tom13.coworkingservice.out.utils.security.PasswordUtil.hashPassword;

@WebServlet("/admin/user/edit/password")
public class EditUserPasswordAdministratorServlet extends AdministrationServlet {

    public static final String NEW_PASSWORD = "newPassword";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasRole(Role.ADMINISTRATOR)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
            return;
        }
        String userIdStr = request.getParameter(USER_ID);
        String newPassword = request.getParameter(NEW_PASSWORD);
        Optional<Long> userIdOpt = getLongParam(userIdStr);
        if (userIdOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат параметра запроса");
            return;
        }
        if (newPassword == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Отсутствует параметр %s", NEW_PASSWORD));
            return;
        }
        String hashPassword = hashPassword(newPassword);
        try {
            administrationService.editUserPasswordByAdministrator(userIdOpt.get(), hashPassword);

            String responseSuccess = String.format("Пароль пользователя с ID: %s успешно изменен", userIdOpt.get());

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
