package io.ylab.tom13.coworkingservice.out.rest.servlet.user;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.servlet.UserServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.out.utils.security.PasswordUtil.hashPassword;

/**
 * Сервлет для изменения пароля пользователя.
 * URL: /user/edit/password
 */
@WebServlet("/user/edit/password")
public class UserEditPasswordServlet extends UserServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected UserEditPasswordServlet(UserRepository userRepository) {
        super(userRepository);
    }
//
//    /**
//     * Обрабатывает POST запрос для изменения пароля пользователя.
//     *
//     * @param request  HTTP запрос
//     * @param response HTTP ответ
//     * @throws IOException в случае ошибки ввода-вывода
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasAuthenticated()) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//        String jsonRequest = getJsonRequest(request);
//        PasswordChangeDTO dtoFromRequest = objectMapper.readValue(jsonRequest, PasswordChangeDTO.class);
//        String hashPassword = hashPassword(dtoFromRequest.newPassword());
//        PasswordChangeDTO changeDTO = new PasswordChangeDTO(dtoFromRequest.email(), dtoFromRequest.oldPassword(), hashPassword);
//
//        try {
//            userEditService.editPassword(changeDTO);
//            String responseSuccess = String.format("Пароль пользователя с email: %s успешно изменен!",
//                    dtoFromRequest.email());
//            setJsonResponse(response, responseSuccess);
//        } catch (UnauthorizedException e) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        } catch (UserNotFoundException e) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        } catch (UserAlreadyExistsException e) {
//            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
//        }
//
//    }
}
