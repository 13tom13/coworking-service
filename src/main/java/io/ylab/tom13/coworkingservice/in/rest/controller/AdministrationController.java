package io.ylab.tom13.coworkingservice.in.rest.controller;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;

import java.util.List;

public interface AdministrationController {

    ResponseDTO<List<UserDTO>> getAllUsers();

    ResponseDTO<UserDTO> getUserByEmail(String email);

    ResponseDTO<UserDTO> editUserByAdministrator(UserDTO userDTO);

    ResponseDTO<String> editUserPasswordByAdministrator(long userId, String newHashPassword);

    ResponseDTO<String> registrationUser(RegistrationDTO registrationDTO, Role role);
}
