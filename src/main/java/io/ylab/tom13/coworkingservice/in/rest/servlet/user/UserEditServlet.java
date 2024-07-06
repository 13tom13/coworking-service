package io.ylab.tom13.coworkingservice.in.rest.servlet.user;


import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.servlet.UserServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.in.utils.security.SecurityController.hasAuthenticated;

@WebServlet("/user/edit")
public class UserEditServlet extends UserServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String jsonRequest = getJsonRequest(request);
        UserDTO userFromRequest = objectMapper.readValue(jsonRequest, UserDTO.class);

        try {
            UserDTO userFromResponse = userEditService.editUser(userFromRequest);
            String responseSuccess = String.format("Имя: %s фамилия: %s email: %s ",
                    userFromResponse.firstName(), userFromResponse.lastName(), userFromResponse.email());
            setJsonResponse(response, responseSuccess);
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (UserNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (UserAlreadyExistsException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        }
    }
}
