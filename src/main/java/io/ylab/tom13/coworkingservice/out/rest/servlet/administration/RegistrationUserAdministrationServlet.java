package io.ylab.tom13.coworkingservice.out.rest.servlet.administration;

import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.servlet.AdministrationServlet;
import jakarta.servlet.annotation.WebServlet;

/**
 * Сервлет для регистрации пользователя администратором.
 */
@WebServlet("/admin/user/registration")
public class RegistrationUserAdministrationServlet extends AdministrationServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected RegistrationUserAdministrationServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /**
//     * Обрабатывает HTTP POST запрос для регистрации нового пользователя.
//     *
//     * @param request  HTTP запрос, содержащий данные нового пользователя в формате JSON
//     * @param response HTTP ответ, который будет содержать результат регистрации в формате JSON
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasRole(Role.ADMINISTRATOR)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, new NoAccessException().getMessage());
//            return;
//        }
//
//        String jsonRequest = getJsonRequest(request);
//        RegistrationDTO registrationDTO = objectMapper.readValue(jsonRequest, RegistrationDTO.class);
//        String hashPassword = hashPassword(registrationDTO.password());
//        RegistrationDTO registrationWithHashPassword = RegistrationMapper.INSTANCE.withNewPassword(registrationDTO, hashPassword);
//
//        try {
//            administrationService.registrationUser(registrationWithHashPassword);
//            String responseSuccess = String.format("Пользователь с именем: %s %s и email: %s успешно зарегистрирован",
//                    registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email());
//
//            setJsonResponse(response, responseSuccess, HttpServletResponse.SC_CREATED);
//
//        } catch (UserAlreadyExistsException e) {
//            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
}