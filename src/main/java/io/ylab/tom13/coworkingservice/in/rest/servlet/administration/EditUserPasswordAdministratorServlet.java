package io.ylab.tom13.coworkingservice.in.rest.servlet.administration;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.servlet.AdministrationServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.in.utils.security.PasswordUtil.hashPassword;
import static io.ylab.tom13.coworkingservice.in.utils.security.SecurityController.hasRole;

@WebServlet("/admin/user/edit/password")
public class EditUserPasswordAdministratorServlet extends AdministrationServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasRole(Role.ADMINISTRATOR)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
            return;
        }

        String userIdStr = request.getParameter("userId");
        String newPassword = request.getParameter("newPassword");


        if (userIdStr == null || newPassword == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует параметр userId или newHashPassword");
            return;
        }


        long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат идентификатора пользователя");
            return;
        }

        String hashPassword = hashPassword(newPassword);

        try {
            administrationService.editUserPasswordByAdministrator(userId, hashPassword);

            String responseSuccess = String.format("Пароль пользователя с ID: %s успешно изменен", userId);

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
