package io.ylab.tom13.coworkingservice.out.rest.services.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.out.utils.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.ylab.tom13.coworkingservice.out.security.PasswordUtil.hashPassword;
import static io.ylab.tom13.coworkingservice.out.security.PasswordUtil.verifyPassword;

/**
 * Реализация интерфейса {@link UserEditService}.
 * Сервиса для редактирования пользовательских данных.
 */
@Service
@RequiredArgsConstructor
public class UserEditServiceImpl implements UserEditService {

    private final UserRepository userRepository;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO editUser(UserDTO userDTO){

        User existingUser = userRepository.findById(userDTO.id()).orElseThrow(() -> new UserNotFoundException("с ID " + userDTO.id()));

        String newEmail = userDTO.email();
        String existingEmail = existingUser.email();
        if (!newEmail.equals(existingEmail) && userRepository.findByEmail(newEmail).isPresent()) {
            throw new UserAlreadyExistsException(newEmail);
        }

        User userToUpdate = userMapper.toUserWithEmailAndPassword(userDTO, newEmail, existingUser.password());

        Optional<User> updatedUser = userRepository.updateUser(userToUpdate);

        if (userRepository.findById(userToUpdate.id()).isEmpty() || userRepository.findByEmail(userToUpdate.email()).isEmpty() || updatedUser.isEmpty()) {
            throw new RepositoryException("Не удалось обновить пользователя с ID " + userDTO.id());
        }

        return userMapper.toUserDTO(updatedUser.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editPassword(PasswordChangeDTO passwordChangeDTO) {
        String email = passwordChangeDTO.email();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("с email " + email));
        String newPassword = hashPassword(passwordChangeDTO.newPassword());
        checkPassword(passwordChangeDTO.oldPassword(), user.password());
        User updatedUser = userMapper.toUserWithPassword(user, newPassword);
        userRepository.updateUser(updatedUser);
    }

    /**
     * Проверяет соответствие пароля из DTO паролю пользователя.
     *
     * @param passwordFromDTO пароль из DTO для проверки.
     * @param passwordFromRep пароль пользователя из репозитория.
     * @throws UnauthorizedException если старый пароль не совпадает с паролем пользователя.
     */
    private void checkPassword(String passwordFromDTO, String passwordFromRep) {
        if (!verifyPassword(passwordFromDTO, passwordFromRep)) {
            throw new UnauthorizedException("Старый пароль не совпадает");
        }

    }
}

