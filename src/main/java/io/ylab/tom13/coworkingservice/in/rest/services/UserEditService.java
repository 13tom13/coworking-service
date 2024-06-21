package io.ylab.tom13.coworkingservice.in.rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;

public interface UserEditService {
    UserDTO editUser(UserDTO userDTO) throws RepositoryException;

    void editPassword(String email, String oldPassword, String newPassword) throws UnauthorizedException, RepositoryException;
}
