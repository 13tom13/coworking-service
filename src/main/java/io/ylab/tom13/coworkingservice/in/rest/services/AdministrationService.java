package io.ylab.tom13.coworkingservice.in.rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;

import java.util.List;

public interface AdministrationService {

    List<UserDTO> getAllUsers();

    UserDTO getUserByEmail(String email) throws UserNotFoundException;

    UserDTO editUserByAdministrator(UserDTO userDTO) throws UserNotFoundException, RepositoryException;

    void editUserPasswordByAdministrator(long userId, String newHashPassword) throws UserNotFoundException, RepositoryException;

    void registrationUser(RegistrationDTO registrationDTO, Role role) throws RepositoryException;
}
