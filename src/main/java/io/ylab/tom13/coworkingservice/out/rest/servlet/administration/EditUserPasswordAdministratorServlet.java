package io.ylab.tom13.coworkingservice.out.rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.servlet.AdministrationServlet;
import jakarta.servlet.annotation.WebServlet;

/**
 * Сервлет для изменения пароля пользователя администратором.
 */
@WebServlet("/admin/user/edit/password")
public class EditUserPasswordAdministratorServlet extends AdministrationServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected EditUserPasswordAdministratorServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    public static final String NEW_PASSWORD = "newPassword";
//
//    /**
//     * Обрабатывает HTTP POST запрос для изменения пароля пользователя.
//     *
//     * @param request  HTTP запрос, содержащий параметры изменения пароля
//     * @param response HTTP ответ, который будет отправлен клиенту
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasRole(Role.ADMINISTRATOR)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
//            return;
//        }
//
//        String userIdStr = request.getParameter(USER_ID);
//        String newPassword = request.getParameter(NEW_PASSWORD);
//
//        Optional<Long> userIdOpt = getLongParam(userIdStr);
//        if (userIdOpt.isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат параметра запроса");
//            return;
//        }
//
//        if (newPassword == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Отсутствует параметр %s", NEW_PASSWORD));
//            return;
//        }
//
//        String hashPassword = hashPassword(newPassword);
//        try {
//            administrationService.editUserPasswordByAdministrator(userIdOpt.get(), hashPassword);
//
//            String responseSuccess = String.format("Пароль пользователя с ID: %s успешно изменен", userIdOpt.get());
//
//            setJsonResponse(response, responseSuccess);
//
//        } catch (UserAlreadyExistsException e) {
//            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        } catch (UserNotFoundException e) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        }
//    }
}
