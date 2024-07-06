package io.ylab.tom13.coworkingservice.in.rest.servlet.administration;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.servlet.AdministrationServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static io.ylab.tom13.coworkingservice.in.utils.security.SecurityController.hasRole;

@WebServlet("/admin/users")
public class GetAllUsersServlet extends AdministrationServlet {

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
