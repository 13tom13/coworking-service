package io.ylab.tom13.coworkingservice.out.rest.servlet.authorization;


import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.AuthorizationServiceImpl;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServiceServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет для авторизации пользователя.
 */
@WebServlet("/authorization")
public class AuthorizationServlet extends CoworkingServiceServlet {

    private final AuthorizationService authorizationService;

    /**
     * Конструктор класса. Инициализирует сервис авторизации.
     */
    public AuthorizationServlet() {
        this.authorizationService = new AuthorizationServiceImpl();
    }

    /**
     * Обрабатывает HTTP POST запрос для авторизации пользователя.
     *
     * @param request  HTTP запрос, содержащий данные авторизации пользователя в формате JSON
     * @param response HTTP ответ, который будет содержать результат авторизации в формате JSON
     * @throws IOException если возникает ошибка при чтении или записи данных
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jsonRequest = getJsonRequest(request);
        AuthorizationDTO authorizationDTO = objectMapper.readValue(jsonRequest, AuthorizationDTO.class);

        try {
            UserDTO user = authorizationService.login(authorizationDTO);
            localSession.setUser(user);
            String responseSuccess = String.format("Пользователь с именем: %s %s и email: %s успешно авторизирован",
                    user.firstName(), user.lastName(), user.email());

            setJsonResponse(response, responseSuccess);
        } catch (UnauthorizedException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
