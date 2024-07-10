package io.ylab.tom13.coworkingservice.out.rest.servlet.registration;


import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.RegistrationServiceImpl;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServiceServlet;
import io.ylab.tom13.coworkingservice.out.utils.mapper.RegistrationMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.ylab.tom13.coworkingservice.out.utils.security.PasswordUtil.hashPassword;

/**
 * Сервлет для регистрации новых пользователей.
 */
@WebServlet("/registration")
public class RegistrationServlet extends CoworkingServiceServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected RegistrationServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    private final RegistrationService registrationService;
//
//    /**
//     * Инициализирует сервлет и устанавливает экземпляр сервиса регистрации.
//     */
//    public RegistrationServlet() {
//        registrationService = new RegistrationServiceImpl();
//    }
//
//    /**
//     * Обрабатывает HTTP POST запрос для регистрации нового пользователя.
//     *
//     * @param request  HTTP запрос, содержащий данные пользователя в формате JSON
//     * @param response HTTP ответ, который будет содержать сообщение об успешной регистрации или ошибку
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String jsonRequest = getJsonRequest(request);
//        RegistrationDTO registrationDTO = objectMapper.readValue(jsonRequest, RegistrationDTO.class);
//        String hashPassword = hashPassword(registrationDTO.password());
//        RegistrationDTO registrationWithHashPassword = RegistrationMapper.INSTANCE.withNewPassword(registrationDTO, hashPassword);
//        try {
//            UserDTO user = registrationService.createUser(registrationWithHashPassword);
//            String responseSuccess = String.format("Пользователь с именем: %s %s и email: %s успешно зарегистрирован",
//                    user.firstName(), user.lastName(), user.email());
//            setJsonResponse(response, responseSuccess, HttpServletResponse.SC_CREATED);
//        } catch (UserAlreadyExistsException e) {
//            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
}
