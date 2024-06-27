package io.ylab.tom13.coworkingservice.out.client;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.rest.controller.UserEditController;
import io.ylab.tom13.coworkingservice.in.rest.controller.implementation.UserEditControllerImpl;
import io.ylab.tom13.coworkingservice.out.exceptions.EditException;

public class UserEditClient extends Client {

    private final UserEditController userEditController;

    public UserEditClient() {
        userEditController = new UserEditControllerImpl();
    }


    public UserDTO editUser(UserDTO userDTO) throws EditException {
        ResponseDTO<UserDTO> response = userEditController.editUser(userDTO);
        if (response.success()) {
            return response.data();
        } else {
            throw new EditException(response.message());
        }
    }

    public String editPassword(PasswordChangeDTO passwordChangeDTO) throws EditException {
        ResponseDTO<String> response = userEditController.editPassword(passwordChangeDTO);
        if (response.success()) {
           return response.data();
        } else {
            throw new EditException(response.message());
        }
    }
}
