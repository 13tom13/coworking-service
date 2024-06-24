package io.ylab.tom13.coworkingservice.in.rest.services.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;

import java.util.List;

public interface AdministrationService {

    List<UserDTO> getAllUsers(AuthenticationDTO authenticationDTO) throws NoAccessException;
}
