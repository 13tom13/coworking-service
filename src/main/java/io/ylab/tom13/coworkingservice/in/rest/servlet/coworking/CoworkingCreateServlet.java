package io.ylab.tom13.coworkingservice.in.rest.servlet.coworking;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.in.utils.security.SecurityController.hasRole;

@WebServlet("/coworking/create")
public class CoworkingCreateServlet extends CoworkingServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
            return;
        }

        String jsonRequest = getJsonRequest(request);
        try {
            CoworkingDTO coworkingDTO = objectMapper.readValue(jsonRequest, CoworkingDTO.class);
            CoworkingDTO coworking = coworkingService.createCoworking(coworkingDTO);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString("Коворкинг успешно создан: \n"));
            response.getWriter().write(objectMapper.writeValueAsString(coworking));
        } catch (InvalidTypeIdException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не верный тип объекта: " + e.getTypeId());
        } catch (CoworkingConflictException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

