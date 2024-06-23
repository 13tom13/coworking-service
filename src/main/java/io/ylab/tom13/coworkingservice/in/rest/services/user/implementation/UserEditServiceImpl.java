package io.ylab.tom13.coworkingservice.in.rest.services.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.user.UserEditService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;

public class UserEditServiceImpl implements UserEditService {

    private final UserRepository userRepository;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserEditServiceImpl() {
        this.userRepository = UserRepositoryCollection.getInstance();
    }

    @Override
    public UserDTO editUser(UserDTO userDTO) throws RepositoryException {
        return userRepository.updateUser(userDTO);
    }

    @Override
    public void editPassword(PasswordChangeDTO passwordChangeDTO) throws UnauthorizedException, RepositoryException {
        userRepository.updatePassword(passwordChangeDTO);
    }
}

