package io.ylab.tom13.coworkingservice.in.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.controller.UserEditController;
import io.ylab.tom13.coworkingservice.in.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.UserEditServiceImpl;
import io.ylab.tom13.coworkingservice.in.utils.SecurityController;

public class UserEditControllerImpl extends SecurityController implements UserEditController {

    private final UserEditService userEditService;

    public UserEditControllerImpl() {
        userEditService = new UserEditServiceImpl();
    }

    @Override
    public ResponseDTO<UserDTO> editUser(UserDTO userDTO) {
        if (!hasAuthenticated()){
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            UserDTO user = userEditService.editUser(userDTO);
            return ResponseDTO.success(user);
        } catch (RepositoryException | UserNotFoundException | UserAlreadyExistsException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<String> editPassword(PasswordChangeDTO passwordChangeDTO) {
        if (!hasAuthenticated()){
            return ResponseDTO.failure(new UnauthorizedException().getMessage());
        }
        try {
            userEditService.editPassword(passwordChangeDTO);
            return ResponseDTO.success("Пароль успешно изменен");
        } catch (UnauthorizedException | RepositoryException | UserNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}
