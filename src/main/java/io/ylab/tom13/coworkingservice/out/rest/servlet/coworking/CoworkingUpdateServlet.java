package io.ylab.tom13.coworkingservice.out.rest.servlet.coworking;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.out.utils.security.SecurityController.hasRole;

@WebServlet("/coworking/update")
public class CoworkingUpdateServlet extends CoworkingServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
            return;
        }

        String jsonRequest = getJsonRequest(request);
        try {
            CoworkingDTO coworkingDTO = objectMapper.readValue(jsonRequest, CoworkingDTO.class);
            CoworkingDTO coworking = coworkingService.updateCoworking(coworkingDTO);
            setJsonResponse(response, coworking);
        } catch (InvalidTypeIdException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не верный тип объекта: " + e.getTypeId());
        } catch (CoworkingConflictException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (RepositoryException | CoworkingUpdatingExceptions e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (CoworkingNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
