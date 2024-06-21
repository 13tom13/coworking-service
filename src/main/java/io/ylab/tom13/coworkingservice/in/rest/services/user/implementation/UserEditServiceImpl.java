package io.ylab.tom13.coworkingservice.in.rest.services.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.user.UserEditService;
import io.ylab.tom13.coworkingservice.in.utils.UserMapper;
import org.mindrot.jbcrypt.BCrypt;

public class UserEditServiceImpl implements UserEditService {

    private final UserRepository userRepository;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserEditServiceImpl() {
        this.userRepository = UserRepositoryCollection.getInstance();
    }

    @Override
    public UserDTO editUser(UserDTO userDTO) throws RepositoryException {
        long id = userDTO.id();
        String hashPassword = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(userDTO.email())).password();
        User newUser = userMapper.toUser(userDTO, hashPassword);
        userRepository.updateUser(newUser);
        return userMapper.toUserDTO(newUser);
    }

    @Override
    public void editPassword(String email, String oldPassword, String newPassword) throws UnauthorizedException, RepositoryException {
        User userFromRep = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(email));
        if (BCrypt.checkpw(oldPassword, userFromRep.password())) {
            User newUser = userMapper.toUser(userFromRep, newPassword);
            userRepository.updateUser(newUser);
        } else {
            throw new UnauthorizedException();
        }
    }
}

