package io.ylab.tom13.coworkingservice.out.rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.AdministrationServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.out.utils.security.SecurityController.hasRole;

@WebServlet("/admin/user")
public class GetUserByEmailServlet extends AdministrationServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasRole(Role.ADMINISTRATOR)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
            return;
        }
        String email = request.getParameter("email");

        try {
            UserDTO user = administrationService.getUserByEmail(email);

            setJsonResponse(response, user);
        } catch (UserNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
