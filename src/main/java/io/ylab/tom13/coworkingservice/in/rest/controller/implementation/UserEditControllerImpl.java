package io.ylab.tom13.coworkingservice.in.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.controller.UserEditController;
import io.ylab.tom13.coworkingservice.in.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.UserEditServiceImpl;

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
        } catch (RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<String> editPassword(String email, String oldPassword, String newPassword) {
        try {
            userEditService.editPassword(email, oldPassword, newPassword);
            return ResponseDTO.success("Пароль успешно изменен");
        } catch (UnauthorizedException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}
