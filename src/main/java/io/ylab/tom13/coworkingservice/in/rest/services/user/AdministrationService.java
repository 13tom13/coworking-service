package io.ylab.tom13.coworkingservice.in.rest.services.user;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;

import java.util.List;

public interface AdministrationService {

    List<UserDTO> getAllUsers(AuthenticationDTO authenticationDTO) throws NoAccessException;

    UserDTO getUserByEmail(AuthenticationDTO authentication, String email) throws UserNotFoundException, NoAccessException;

    UserDTO editUserByAdministrator(AuthenticationDTO authentication, UserDTO userDTO) throws UserNotFoundException, NoAccessException, RepositoryException;

    void editUserPasswordByAdministrator(AuthenticationDTO authentication, long userId, String newHashPassword) throws NoAccessException, UserNotFoundException, RepositoryException;
}
