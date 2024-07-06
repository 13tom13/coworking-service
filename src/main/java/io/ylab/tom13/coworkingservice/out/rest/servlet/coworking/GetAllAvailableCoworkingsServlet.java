package io.ylab.tom13.coworkingservice.out.rest.servlet.coworking;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static io.ylab.tom13.coworkingservice.out.utils.security.SecurityController.hasAuthenticated;

@WebServlet("/coworking/available")
public class GetAllAvailableCoworkingsServlet extends CoworkingServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new UnauthorizedException().getMessage());
            return;
        }
        try {
            Map<String, CoworkingDTO> availableCoworkings = coworkingService.getAllAvailableCoworkings();
            setJsonResponse(response, availableCoworkings);
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
