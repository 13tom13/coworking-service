package io.ylab.tom13.coworkingservice.in.rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;

public interface UserEditController {

    ResponseDTO<UserDTO> editUser (UserDTO userDTO);

    ResponseDTO<String> editPassword (PasswordChangeDTO passwordChangeDTO);

}

