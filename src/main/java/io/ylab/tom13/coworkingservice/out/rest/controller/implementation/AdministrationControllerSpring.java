package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.controller.AdministrationController;
import io.ylab.tom13.coworkingservice.out.rest.services.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.ylab.tom13.coworkingservice.out.security.PasswordUtil.hashPassword;

/**
 * Реализация интерфейса {@link AdministrationController}.
 * Этот класс обеспечивает выполнение административных функций для управления пользователями в системе.
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "Administration Controller", description = "API для управления пользователями и администраторами")
public class AdministrationControllerSpring implements AdministrationController {

    private final AdministrationService administrationService;

    /**
     * Конструктор для инициализации объекта и подключения к службе администрирования.
     */
    @Autowired
    public AdministrationControllerSpring(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей получен",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)))
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> allUsers = administrationService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Получить пользователя по email", description = "Возвращает пользователя по email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestParam(name = "email") String email) {
        try {
            UserDTO user = administrationService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Редактировать пользователя", description = "Редактирует данные пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь отредактирован",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PatchMapping("/user/edit")
    public ResponseEntity<?> editUserByAdministrator(@RequestBody UserDTO userDTO) {
        try {
            UserDTO user = administrationService.editUserByAdministrator(userDTO);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Изменить пароль пользователя", description = "Изменяет пароль пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль пользователя изменен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PatchMapping("/user/edit/password")
    public ResponseEntity<?> editUserPasswordByAdministrator(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        try {
            String responseSuccess = "Пароль пользователя успешно изменен";
            String hashPassword = hashPassword(passwordChangeDTO.newPassword());
            administrationService.editUserPasswordByAdministrator(passwordChangeDTO.email(), hashPassword);
            return ResponseEntity.ok(responseSuccess);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Operation(summary = "Регистрация пользователя", description = "Регистрирует нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping("/user/registration")
    public ResponseEntity<?> registrationUser(@RequestBody RegistrationDTO registrationDTO) {
        try {
            UserDTO registrationUser = administrationService.registrationUser(registrationDTO);
            return ResponseEntity.ok(registrationUser);
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}