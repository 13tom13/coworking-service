package io.ylab.tom13.coworkingservice.in.rest.controller.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;

import java.util.List;

public interface AdministrationController {

    ResponseDTO<List<UserDTO>> getAllUsers(AuthenticationDTO authenticationDTO);

    ResponseDTO<UserDTO> getUserByEmail(AuthenticationDTO authentication, String email);

    ResponseDTO<UserDTO> editUserByAdministrator(AuthenticationDTO authentication, UserDTO userDTO);

    ResponseDTO<String> editUserPasswordByAdministrator(AuthenticationDTO authentication, long userId, String newHashPassword);
}
