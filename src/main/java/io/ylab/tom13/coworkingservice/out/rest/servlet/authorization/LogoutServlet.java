package io.ylab.tom13.coworkingservice.out.rest.servlet.authorization;

import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServiceServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет для выхода пользователя из системы (деавторизации).
 */
@WebServlet("/logout")
public class LogoutServlet extends CoworkingServiceServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected LogoutServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /**
//     * Обрабатывает HTTP GET запрос для выхода пользователя из системы.
//     * Если пользователь авторизован, происходит его деавторизация.
//     *
//     * @param request  HTTP запрос
//     * @param response HTTP ответ, который будет содержать результат деавторизации в формате JSON
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (localSession.getUser().isPresent()) {
//            UserDTO user = localSession.getUser().get();
//            String responseSuccess = String.format("Пользователь с именем: %s %s и email: %s деавторизирован",
//                    user.firstName(), user.lastName(), user.email());
//            localSession.removeUser();
//            setJsonResponse(response, responseSuccess);
//        } else {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
//        }
//    }
}