package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.UserEditController;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.UserEditServiceImpl;
import io.ylab.tom13.coworkingservice.out.utils.security.SecurityHTTPController;

/**
 * Реализация интерфейса {@link UserEditController}.
 * Обрабатывает запросы на редактирование пользователя.
 */
public class UserEditControllerImpl extends SecurityHTTPController implements UserEditController {

    private final UserEditService userEditService;

    /**
     * Конструктор для инициализации контроллера редактирования пользователей.
     */
    public UserEditControllerImpl() {
        userEditService = new UserEditServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<UserDTO> editUser(UserDTO userDTO) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            UserDTO user = userEditService.editUser(userDTO);
            return ResponseDTO.success(user);
        } catch (RepositoryException | UserNotFoundException | UserAlreadyExistsException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<String> editPassword(PasswordChangeDTO passwordChangeDTO) {
        if (!hasAuthenticated()) {
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            userEditService.editPassword(passwordChangeDTO);
            return ResponseDTO.success("Пароль успешно изменен");
        } catch (UnauthorizedException | RepositoryException | UserNotFoundException | UserAlreadyExistsException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}
