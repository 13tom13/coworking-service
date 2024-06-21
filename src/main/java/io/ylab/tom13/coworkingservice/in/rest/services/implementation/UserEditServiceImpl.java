package io.ylab.tom13.coworkingservice.in.rest.services.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.UserEditService;
import org.mindrot.jbcrypt.BCrypt;

public class UserEditServiceImpl implements UserEditService {

    private final UserRepository userRepository;

    public UserEditServiceImpl() {
        this.userRepository = UserRepositoryCollection.getInstance();
    }

    @Override
    public UserDTO editUser(UserDTO userDTO) throws RepositoryException {
        long id = userDTO.id();
        String hashPassword = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(userDTO.email())).getPassword();
        User newUser = new User(userDTO.id(), userDTO.firstName(), userDTO.lastName(), userDTO.email(), hashPassword);
        userRepository.updateUser(newUser);
        return new UserDTO(newUser.getId(), newUser.getFirstName(), newUser.getLastName(), newUser.getEmail());
    }

    @Override
    public void editPassword(String email, String oldPassword, String newPassword) throws UnauthorizedException, RepositoryException {
        User userFromRep = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(email));
        if (BCrypt.checkpw(oldPassword, userFromRep.getPassword())) {
            User newUser = new User(userFromRep.getId(), userFromRep.getFirstName(), userFromRep.getLastName(), userFromRep.getEmail(), newPassword);
            userRepository.updateUser(newUser);
        } else {
            throw new UnauthorizedException();
        }
    }
}

