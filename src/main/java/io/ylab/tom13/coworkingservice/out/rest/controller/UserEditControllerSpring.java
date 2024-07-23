package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер редактирования пользователя.
 * Обрабатывает запросы на редактирование пользователя.
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Контроллер редактирования пользователей", description = "Обрабатывает запросы на редактирование пользователя.")
public class UserEditControllerSpring {

    private final UserEditService userEditService;

    /**
     * Редактирует информацию о пользователе на основе предоставленного объекта UserDTO.
     *
     * @param userDTO DTO с информацией о пользователе для редактирования.
     * @return ResponseEntity с обновленным объектом UserDTO или сообщением об ошибке.
     */
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
        UserDTO user = userEditService.editUser(userDTO);
        return ResponseEntity.ok(user);
    }

    /**
     * Изменяет пароль пользователя на основе предоставленного объекта PasswordChangeDTO.
     *
     * @param passwordChangeDTO DTO с новым паролем и идентификатором пользователя.
     * @return ResponseEntity с сообщением об успешном изменении пароля или сообщением об ошибке.
     */
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
        userEditService.editPassword(passwordChangeDTO);
        return ResponseEntity.ok().build();
    }
}
