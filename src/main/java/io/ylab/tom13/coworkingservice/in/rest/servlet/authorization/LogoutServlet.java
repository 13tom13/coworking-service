package io.ylab.tom13.coworkingservice.in.rest.servlet.authorization;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.rest.servlet.CoworkingServiceServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends CoworkingServiceServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (localSession.getUser() != null) {
            UserDTO user = localSession.getUser();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            String responseSuccess = String.format("Пользователь с именем: %s %s и email: %s деавторизирован",
                    user.firstName(), user.lastName(), user.email());
            response.getWriter().write(objectMapper.writeValueAsString(responseSuccess));
            localSession.removeUser();
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
        }
    }
}
