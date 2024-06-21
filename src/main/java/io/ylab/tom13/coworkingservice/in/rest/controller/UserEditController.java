package io.ylab.tom13.coworkingservice.in.rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;

public interface UserEditController {

    ResponseDTO<UserDTO> editUser (UserDTO userDTO);

    ResponseDTO<String> editPassword (String email, String oldPassword, String newPassword);

}

