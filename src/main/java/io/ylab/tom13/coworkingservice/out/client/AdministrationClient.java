package io.ylab.tom13.coworkingservice.out.client;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.controller.AdministrationController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.AdministrationControllerImpl;
import io.ylab.tom13.coworkingservice.out.exceptions.EditException;

import java.util.List;

public class AdministrationClient extends Client {

    private final AdministrationController administrationController;

    public AdministrationClient() {
        administrationController = new AdministrationControllerImpl();
    }


    public List<UserDTO> getUserList() {
        ResponseDTO<List<UserDTO>> allUsers = administrationController.getAllUsers();
        return allUsers.data();
    }

    public UserDTO getUserByEmail(String email) throws UserNotFoundException {
        ResponseDTO<UserDTO> user = administrationController.getUserByEmail(email);
        if (user.success()) {
            return user.data();
        } else {
            throw new UserNotFoundException(user.message());
        }
    }

    public UserDTO editUserByAdministrator(UserDTO userDTO) throws EditException {
        ResponseDTO<UserDTO> user = administrationController.editUserByAdministrator(userDTO);
        if (user.success()) {
            return user.data();
        } else {
            throw new EditException(user.message());
        }
    }

    public String editPasswordByAdministrator(long userId, String newHashPassword) throws EditException {
        ResponseDTO<String> response = administrationController.editUserPasswordByAdministrator(userId, newHashPassword);
        if (response.success()) {
            return response.data();
        } else {
            throw new EditException(response.message());
        }
    }

    public void registrationUser(RegistrationDTO registrationDTO) throws EditException {
        ResponseDTO<String> response = administrationController.registrationUser(registrationDTO);
        if (!response.success()) {
            throw new EditException(response.message());
        }
    }
}
