package io.ylab.tom13.coworkingservice.in.rest.services.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserEditServiceImpl implements UserEditService {

    private final UserRepository userRepository;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserEditServiceImpl() {
        this.userRepository = UserRepositoryCollection.getInstance();
    }

    @Override
    public UserDTO editUser(UserDTO userDTO) throws RepositoryException, UserNotFoundException, UserAlreadyExistsException {
        User existingUser = userRepository.findById(userDTO.id()).orElseThrow(() -> new UserNotFoundException("с ID " + userDTO.id()));

        String newEmail = userDTO.email();
        String existingEmail = existingUser.email();

        if (!newEmail.equals(existingEmail) && userRepository.findByEmail(newEmail).isPresent()) {
            throw new UserAlreadyExistsException(newEmail);
        }

        User userToUpdate = new User(userDTO.id(), userDTO.firstName(), userDTO.lastName(), newEmail, existingUser.password(), userDTO.role());
        Optional<User> updatedUser = userRepository.updateUser(userToUpdate);

        if (userRepository.findById(userToUpdate.id()).isEmpty()
            || userRepository.findByEmail(userToUpdate.email()).isEmpty() || updatedUser.isEmpty()) {
            throw new RepositoryException("Не удалось обновить пользователя с ID " + userDTO.id());
        }

        return userMapper.toUserDTO(updatedUser.get());
    }

    @Override
    public void editPassword(PasswordChangeDTO passwordChangeDTO) throws UnauthorizedException, RepositoryException, UserNotFoundException {
        String email = passwordChangeDTO.email();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("с email " + email));

        checkPassword(passwordChangeDTO.oldPassword(), user.password());

        String newPassword = passwordChangeDTO.newPassword();
        User updatedUser = new User(user.id(), user.firstName(), user.lastName(), user.email(), newPassword, user.role());
        userRepository.updateUser(updatedUser);
    }

    private void checkPassword(String passwordFromDTO, String passwordFromRep) throws UnauthorizedException {
        if (!BCrypt.checkpw(passwordFromDTO, passwordFromRep)) {
            throw new UnauthorizedException("Старый пароля не совпадает");
        }
    }
}

