package io.ylab.tom13.coworkingservice.in.rest.controller.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.controller.user.UserEditController;
import io.ylab.tom13.coworkingservice.in.rest.services.user.UserEditService;
import io.ylab.tom13.coworkingservice.in.rest.services.user.implementation.UserEditServiceImpl;

public class UserEditControllerImpl implements UserEditController {

    private final UserEditService userEditService;

    public UserEditControllerImpl() {
        userEditService = new UserEditServiceImpl();
    }

    @Override
    public ResponseDTO<UserDTO> editUser(UserDTO userDTO) {
        try {
            UserDTO user = userEditService.editUser(userDTO);
            return ResponseDTO.success(user);
        } catch (RepositoryException | UserNotFoundException | UserAlreadyExistsException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<String> editPassword(PasswordChangeDTO passwordChangeDTO) {
        try {
            userEditService.editPassword(passwordChangeDTO);
            return ResponseDTO.success("Пароль успешно изменен");
        } catch (UnauthorizedException | RepositoryException | UserNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}
