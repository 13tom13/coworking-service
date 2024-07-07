package io.ylab.tom13.coworkingservice.out.rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.AdministrationServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.out.utils.security.PasswordUtil.hashPassword;

@WebServlet("/admin/user/registration")
public class RegistrationUserAdministrationServlet extends AdministrationServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasRole(Role.ADMINISTRATOR)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, new NoAccessException().getMessage());
            return;
        }

        String jsonRequest = getJsonRequest(request);
        RegistrationDTO registrationDTO = objectMapper.readValue(jsonRequest, RegistrationDTO.class);
        String hashPassword = hashPassword(registrationDTO.password());
        RegistrationDTO registrationWithHashPassword = new RegistrationDTO(registrationDTO.firstName(), registrationDTO.lastName(),
                registrationDTO.email(), hashPassword, registrationDTO.role());
        try {
            administrationService.registrationUser(registrationWithHashPassword);
            String responseSuccess = String.format("Пользователь с именем: %s %s и email: %s успешно зарегистрирован",
                    registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email());
            setJsonResponse(response, responseSuccess, HttpServletResponse.SC_CREATED);
        } catch (UserAlreadyExistsException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
