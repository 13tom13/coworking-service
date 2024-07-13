package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.UserEditController;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import jakarta.servlet.http.HttpServletResponse;
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
    @PatchMapping("/edit")
    public ResponseEntity<?> editUser(@RequestBody UserDTO userDTO) {
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
    @PatchMapping("/edit/password")
    public ResponseEntity<?> editPassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        try {
            String success = "Пароль успешно изменен";
            PasswordChangeDTO hashPasswordDTO = new PasswordChangeDTO(passwordChangeDTO.email(), passwordChangeDTO.oldPassword(),
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
