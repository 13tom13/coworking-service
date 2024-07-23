package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.rest.services.AdministrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер администратора. Этот класс обеспечивает выполнение административных функций для управления пользователями в системе.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Administration Controller", description = "API для управления пользователями и администраторами")
public class AdministrationControllerSpring {

    private final AdministrationService administrationService;

    /**
     * Получить список всех пользователей системы.
     *
     * @return объект ResponseEntity со списком пользователей или сообщением об ошибке.
     */
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей получен",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)))
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> allUsers = administrationService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    /**
     * Получить пользователя по электронной почте.
     *
     * @param email электронная почта пользователя.
     * @return объект ResponseEntity с данными пользователя или сообщением об ошибке.
     */
    @Operation(summary = "Получить пользователя по email", description = "Возвращает пользователя по email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestParam(name = "email") String email) {
        UserDTO user = administrationService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Редактировать данные пользователя администратором.
     *
     * @param userDTO объект с обновленными данными пользователя.
     * @return объект ResponseEntity с обновленными данными пользователя или сообщением об ошибке.
     */
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
        UserDTO user = administrationService.editUserByAdministrator(userDTO);
        return ResponseEntity.ok(user);
    }

    /**
     * Изменить пароль пользователя администратором.
     *
     * @param passwordChangeDTO DTO с новым паролем и идентификатором пользователя.
     * @return объект ResponseEntity с сообщением об успешной смене пароля или сообщением об ошибке.
     */
    @Operation(summary = "Изменить пароль пользователя", description = "Изменяет пароль пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль пользователя изменен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PatchMapping("/user/edit/password")
    public ResponseEntity<?> editUserPasswordByAdministrator(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        administrationService.editUserPasswordByAdministrator(passwordChangeDTO.email(), passwordChangeDTO.newPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * Зарегистрировать нового пользователя администратором.
     *
     * @param registrationDTO данные нового пользователя для регистрации.
     * @return объект ResponseEntity с сообщением об успешной регистрации или сообщением об ошибке.
     */
    @Operation(summary = "Регистрация пользователя", description = "Регистрирует нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping("/user/registration")
    public ResponseEntity<?> registrationUser(@RequestBody RegistrationDTO registrationDTO) {
        UserDTO registrationUser = administrationService.registrationUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationUser);
    }

}