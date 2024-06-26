package io.ylab.tom13.coworkingservice.out.client;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.controller.user.AdministrationController;
import io.ylab.tom13.coworkingservice.in.rest.controller.user.implementation.AdministrationControllerImpl;
import io.ylab.tom13.coworkingservice.out.exceptions.EditException;

import java.util.List;

public class AdministrationClient extends Client {

    private final AdministrationController administrationController;

    public AdministrationClient() {
        administrationController = new AdministrationControllerImpl();
    }


    public List<UserDTO> getUserList(AuthenticationDTO authentication) {
        ResponseDTO<List<UserDTO>> allUsers = administrationController.getAllUsers(authentication);
        return allUsers.data();
    }

    public UserDTO getUserByEmail(AuthenticationDTO authentication, String email) throws UserNotFoundException {
        ResponseDTO<UserDTO> user = administrationController.getUserByEmail(authentication, email);
        if (user.success()) {
            return user.data();
        } else {
            throw new UserNotFoundException(user.message());
        }
    }

    public UserDTO editUserByAdministrator(AuthenticationDTO authentication, UserDTO userDTO) throws EditException {
        ResponseDTO<UserDTO> user = administrationController.editUserByAdministrator(authentication, userDTO);
        return user.data();
    }

    public String editPasswordByAdministrator(AuthenticationDTO authentication, long userId, String newHashPassword) throws EditException {
        ResponseDTO<String> response = administrationController.editUserPasswordByAdministrator(authentication, userId, newHashPassword);
        if (response.success()) {
            return response.data();
        } else {
            throw new EditException(response.message());
        }
    }

    public String registrationUser(AuthenticationDTO authentication, RegistrationDTO registrationDTO, Role role) throws EditException {
        ResponseDTO<String> response = administrationController.registrationUser(authentication, registrationDTO, role);
        if (response.success()) {
            return response.data();
        } else {
            throw new EditException(response.message());
        }
    }
}
