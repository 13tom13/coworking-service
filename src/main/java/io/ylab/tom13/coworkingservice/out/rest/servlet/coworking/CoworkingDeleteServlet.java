package io.ylab.tom13.coworkingservice.out.rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.out.utils.security.SecurityController.hasRole;

@WebServlet("/coworking/delete")
public class CoworkingDeleteServlet extends CoworkingServlet {

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
            return;
        }

        String coworkingIdStr = request.getParameter("coworkingId");
        if (coworkingIdStr == null || coworkingIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Параметр 'bookingId' пуст или отсутствует");
            return;
        }

        long coworkingId;
        try {
            coworkingId = Long.parseLong(coworkingIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не верный 'coworkingId' параметр");
            return;
        }

        try {
            coworkingService.deleteCoworking(coworkingId);
            String responseSuccess= "Коворкинг успешно удален";
            setJsonResponse(response, responseSuccess);
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (CoworkingNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
