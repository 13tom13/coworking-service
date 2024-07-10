package io.ylab.tom13.coworkingservice.out.rest.servlet.coworking;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.servlet.CoworkingServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет для создания нового рабочего пространства (coworking space).
 */
@WebServlet("/coworking/create")
public class CoworkingCreateServlet extends CoworkingServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected CoworkingCreateServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /**
//     * Обрабатывает HTTP POST запрос для создания нового рабочего пространства.
//     *
//     * @param request  HTTP запрос, содержащий JSON представление данных о рабочем пространстве
//     * @param response HTTP ответ, который будет содержать созданное рабочее пространство в формате JSON
//     * @throws IOException если возникает ошибка при чтении или записи данных
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (!hasRole(Role.ADMINISTRATOR, Role.MODERATOR)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, new UnauthorizedException().getMessage());
//            return;
//        }
//
//        String jsonRequest = getJsonRequest(request);
//        try {
//            CoworkingDTO coworkingDTO = objectMapper.readValue(jsonRequest, CoworkingDTO.class);
//            CoworkingDTO coworking = coworkingService.createCoworking(coworkingDTO);
//            setJsonResponse(response, coworking, HttpServletResponse.SC_CREATED);
//        } catch (InvalidTypeIdException e) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный тип объекта: " + e.getTypeId());
//        } catch (CoworkingConflictException e) {
//            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
//        } catch (RepositoryException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
}