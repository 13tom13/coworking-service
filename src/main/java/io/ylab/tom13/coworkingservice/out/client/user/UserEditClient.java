package io.ylab.tom13.coworkingservice.out.client.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.rest.controller.UserEditController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.UserEditControllerImpl;
import io.ylab.tom13.coworkingservice.out.client.Client;
import io.ylab.tom13.coworkingservice.out.exceptions.EditException;

public class UserEditClient extends Client {

    private final UserEditController userEditController;

    public UserEditClient() {
        userEditController = new UserEditControllerImpl();
    }


    public void editUser(UserDTO userDTO) throws EditException {
        ResponseDTO<UserDTO> response = userEditController.editUser(userDTO);
        if (response.success()) {
            UserDTO user = response.data();
            localSession.setAttribute("user", user);
            System.out.println("Пользователь успешно изменен");
        } else {
            throw new EditException(response.message());
        }
    }

    public void editPassword(String email, String oldPassword, String newHashPassword) throws EditException {
        ResponseDTO<String> response = userEditController.editPassword(email, oldPassword, newHashPassword);
        if (response.success()) {
            System.out.println(response.data());
        } else {
            throw new EditException(response.message());
        }
    }
}
