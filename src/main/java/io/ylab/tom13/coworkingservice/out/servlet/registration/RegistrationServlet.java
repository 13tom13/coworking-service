package io.ylab.tom13.coworkingservice.out.servlet.registration;


import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.rest.services.RegistrationService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.RegistrationServiceImpl;
import io.ylab.tom13.coworkingservice.out.servlet.CoworkingServiceServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.in.utils.security.PasswordUtil.hashPassword;

@WebServlet("/registration")
public class RegistrationServlet extends CoworkingServiceServlet {

    private final RegistrationService registrationService;

    public RegistrationServlet() {

        registrationService = new RegistrationServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonRequest = getJsonRequest(request);
        RegistrationDTO registrationDTO = objectMapper.readValue(jsonRequest, RegistrationDTO.class);
        String hashPassword = hashPassword(registrationDTO.password());
        RegistrationDTO registrationWithHashPassword = new RegistrationDTO(registrationDTO.firstName(),registrationDTO.lastName(),
                registrationDTO.email(),hashPassword);
        try {
            UserDTO user = registrationService.createUser(registrationWithHashPassword);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(String.format("Пользователь с именем: %s %s и email: %s успешно зарегистрирован",
                    user.firstName(), user.lastName(), user.email()));
        } catch (UserAlreadyExistsException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
