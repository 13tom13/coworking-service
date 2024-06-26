package io.ylab.tom13.coworkingservice.in.rest.controller.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.controller.user.AdministrationController;
import io.ylab.tom13.coworkingservice.in.rest.services.user.AdministrationService;
import io.ylab.tom13.coworkingservice.in.rest.services.user.implementation.AdministrationServiceImpl;

import java.util.List;

public class AdministrationControllerImpl implements AdministrationController {

    private final AdministrationService administrationService;

    public AdministrationControllerImpl() {
        administrationService = new AdministrationServiceImpl();
    }

    @Override
    public ResponseDTO<List<UserDTO>> getAllUsers(AuthenticationDTO authenticationDTO) {
        try {
            List<UserDTO> allUsers = administrationService.getAllUsers(authenticationDTO);
            return ResponseDTO.success(allUsers);
        } catch (NoAccessException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<UserDTO> getUserByEmail(AuthenticationDTO authentication, String email) {
        try {
            UserDTO user = administrationService.getUserByEmail(authentication, email);
            return ResponseDTO.success(user);

        } catch (UserNotFoundException | NoAccessException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<UserDTO> editUserByAdministrator(AuthenticationDTO authentication, UserDTO userDTO) {
        try {
            UserDTO user = administrationService.editUserByAdministrator(authentication, userDTO);
            return ResponseDTO.success(user);
        } catch (UserNotFoundException | NoAccessException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<String> editUserPasswordByAdministrator(AuthenticationDTO authentication, long userId, String newHashPassword) {
        try {
            administrationService.editUserPasswordByAdministrator(authentication, userId, newHashPassword);
            return ResponseDTO.success("Пароль успешно изменен");
        } catch (RepositoryException | UserNotFoundException | NoAccessException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<String> registrationUser(AuthenticationDTO authentication, RegistrationDTO registrationDTO, Role role) {
        try {
            administrationService.registrationUser(authentication, registrationDTO, role);
            return ResponseDTO.success("Пользователь успешно создан");
        } catch (RepositoryException | NoAccessException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}
