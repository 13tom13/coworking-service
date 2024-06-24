package io.ylab.tom13.coworkingservice.out.client;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
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
        //test
        return null;
    }

    public void editUserByAdministrator(AuthenticationDTO authentication, UserDTO userDTO) throws EditException {
    }

    public void editPasswordByAdministrator(AuthenticationDTO authentication, long userId, String newHashPassword) throws EditException {
    }
}
