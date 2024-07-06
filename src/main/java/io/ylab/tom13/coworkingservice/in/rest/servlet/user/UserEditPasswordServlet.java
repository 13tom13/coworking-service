package io.ylab.tom13.coworkingservice.in.rest.servlet.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.UserEditServiceImpl;
import io.ylab.tom13.coworkingservice.in.rest.servlet.CoworkingServiceServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.in.utils.security.PasswordUtil.hashPassword;
import static io.ylab.tom13.coworkingservice.in.utils.security.SecurityController.hasAuthenticated;

/**
 * Сервлет для изменения пароля пользователя.
 * URL: /user/edit/password
 */
@WebServlet("/user/edit/password")
public class UserEditPasswordServlet extends CoworkingServiceServlet {

    private final UserEditService userEditService;

    /**
     * Конструктор сервлета, инициализирующий сервис для редактирования пользователя.
     */
    public UserEditPasswordServlet() {
        userEditService = new UserEditServiceImpl();
    }

    /**
     * Обрабатывает POST запрос для изменения пароля пользователя.
     *
     * @param request  HTTP запрос
     * @param response HTTP ответ
     * @throws IOException в случае ошибки ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!hasAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String jsonRequest = getJsonRequest(request);
        PasswordChangeDTO dtoFromRequest = objectMapper.readValue(jsonRequest, PasswordChangeDTO.class);
        String hashPassword = hashPassword(dtoFromRequest.newPassword());
        PasswordChangeDTO changeDTO = new PasswordChangeDTO(dtoFromRequest.email(), dtoFromRequest.oldPassword(), hashPassword);

        try {
            userEditService.editPassword(changeDTO);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(String.format("Пароль пользователя с email: %s успешно изменен!",
                    dtoFromRequest.email()));
        } catch (UnauthorizedException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (UserNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (UserAlreadyExistsException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        }

    }
}
