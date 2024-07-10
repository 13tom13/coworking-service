package io.ylab.tom13.coworkingservice.out.rest.servlet.user;


import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.servlet.UserServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет для редактирования информации о пользователе.
 */
@WebServlet("/user/edit")
public class UserEditServlet extends UserServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected UserEditServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /**
//     * Обрабатывает HTTP POST запрос для редактирования информации о пользователе.
//     *
//     * @param request  HTTP запрос, содержащий данные пользователя в формате JSON
//     * @param response HTTP ответ, который будет содержать сообщение об успешном изменении или ошибку
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasAuthenticated()) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//        String jsonRequest = getJsonRequest(request);
//        UserDTO userFromRequest = objectMapper.readValue(jsonRequest, UserDTO.class);
//
//        try {
//            UserDTO userFromResponse = userEditService.editUser(userFromRequest);
//            String responseSuccess = String.format("Имя: %s фамилия: %s email: %s ",
//                    userFromResponse.firstName(), userFromResponse.lastName(), userFromResponse.email());
//            setJsonResponse(response, responseSuccess);
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        } catch (UserNotFoundException e) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        } catch (UserAlreadyExistsException e) {
//            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
//        }
//    }
}
