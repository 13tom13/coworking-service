package io.ylab.tom13.coworkingservice.out.servlet.authorization;


import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.services.AuthorizationService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.AuthorizationServiceImpl;
import io.ylab.tom13.coworkingservice.out.servlet.CoworkingServiceServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/authorization")
public class AuthorizationServlet extends CoworkingServiceServlet {

    private final AuthorizationService authorizationService;

    public AuthorizationServlet() {
        this.authorizationService = new AuthorizationServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonRequest = getJsonRequest(request);
        AuthorizationDTO authorizationDTO = objectMapper.readValue(jsonRequest, AuthorizationDTO.class);

        try {
            UserDTO userDTO = authorizationService.login(authorizationDTO);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            localSession.setAttribute("user", userDTO);
            response.getWriter().write("Успешная авторизация");
        } catch (UnauthorizedException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }


}
