package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.UserEditController;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.ylab.tom13.coworkingservice.out.security.PasswordUtil.hashPassword;

/**
 * Реализация интерфейса {@link UserEditController}.
 * Обрабатывает запросы на редактирование пользователя.
 */
@RestController
@RequestMapping("/user")
@Tag(name = "Контроллер редактирования пользователей", description = "Обрабатывает запросы на редактирование пользователя.")
public class UserEditControllerSpring implements UserEditController {

    private final UserEditService userEditService;

    /**
     * Конструктор для инициализации контроллера редактирования пользователей.
     */
    @Autowired
    public UserEditControllerSpring(UserEditService userEditService) {
        this.userEditService = userEditService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Редактирование пользователя", description = "Редактирует данные существующего пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно отредактирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/edit")
    public ResponseEntity<?> editUser(
            @Parameter(description = "Данные для редактирования пользователя", required = true)
            @RequestBody UserDTO userDTO) {
        try {
            UserDTO user = userEditService.editUser(userDTO);
            return ResponseEntity.ok(user);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpServletResponse.SC_CONFLICT).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Изменение пароля пользователя", description = "Изменяет пароль существующего пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль успешно изменен"),
            @ApiResponse(responseCode = "403", description = "Запрещено"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/edit/password")
    public ResponseEntity<?> editPassword(
            @Parameter(description = "Данные для изменения пароля пользователя", required = true)
            @RequestBody PasswordChangeDTO passwordChangeDTO) {
        try {
            String success = "Пароль успешно изменен";
            PasswordChangeDTO hashPasswordDTO = new PasswordChangeDTO(
                    passwordChangeDTO.email(), passwordChangeDTO.oldPassword(),
                    hashPassword(passwordChangeDTO.newPassword()));
            userEditService.editPassword(hashPasswordDTO);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "text/plain; charset=UTF-8")
                    .body(success);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpServletResponse.SC_CONFLICT).body(e.getMessage());
        }
    }
}
