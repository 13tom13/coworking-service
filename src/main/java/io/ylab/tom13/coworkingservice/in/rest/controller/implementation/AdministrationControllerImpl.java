package io.ylab.tom13.coworkingservice.in.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.in.rest.controller.AdministrationController;
import io.ylab.tom13.coworkingservice.in.rest.services.AdministrationService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.AdministrationServiceImpl;
import io.ylab.tom13.coworkingservice.in.utils.security.SecurityController;

import java.util.List;

/**
 * Реализация интерфейса {@link AdministrationController}.
 * Этот класс обеспечивает выполнение административных функций для управления пользователями в системе.
 */
public class AdministrationControllerImpl extends SecurityController implements AdministrationController {

    private final AdministrationService administrationService;

    /**
     * Конструктор для инициализации объекта и подключения к службе администрирования.
     */
    public AdministrationControllerImpl() {
        administrationService = new AdministrationServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<List<UserDTO>> getAllUsers() {
        if (!hasRole(Role.ADMINISTRATOR)) {
            return ResponseDTO.failure(new NoAccessException().getMessage());
        }
        List<UserDTO> allUsers = administrationService.getAllUsers();
        return ResponseDTO.success(allUsers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<UserDTO> getUserByEmail(String email) {
        if (!hasRole(Role.ADMINISTRATOR)) {
            return ResponseDTO.failure(new NoAccessException().getMessage());
        }
        try {
            UserDTO user = administrationService.getUserByEmail(email);
            return ResponseDTO.success(user);
        } catch (UserNotFoundException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<UserDTO> editUserByAdministrator(UserDTO userDTO) {
        if (!hasRole(Role.ADMINISTRATOR)) {
            return ResponseDTO.failure(new NoAccessException().getMessage());
        }
        try {
            UserDTO user = administrationService.editUserByAdministrator(userDTO);
            return ResponseDTO.success(user);
        } catch (UserNotFoundException | RepositoryException | UserAlreadyExistsException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<String> editUserPasswordByAdministrator(long userId, String newHashPassword) {
        if (!hasRole(Role.ADMINISTRATOR)) {
            return ResponseDTO.failure(new NoAccessException().getMessage());
        }
        try {
            administrationService.editUserPasswordByAdministrator(userId, newHashPassword);
            return ResponseDTO.success("Пароль успешно изменен");
        } catch (RepositoryException | UserNotFoundException | UserAlreadyExistsException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDTO<String> registrationUser(RegistrationDTO registrationDTO, Role role) {
        if (!hasRole(Role.ADMINISTRATOR)) {
            return ResponseDTO.failure(new NoAccessException().getMessage());
        }
        try {
            administrationService.registrationUser(registrationDTO, role);
            return ResponseDTO.success("Пользователь успешно создан");
        } catch (RepositoryException | UserAlreadyExistsException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}

