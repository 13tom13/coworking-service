package io.ylab.tom13.coworkingservice.in.rest.services.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryJdbc;
import io.ylab.tom13.coworkingservice.in.rest.services.UserEditService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;

import java.sql.SQLException;
import java.util.Optional;

import static io.ylab.tom13.coworkingservice.in.database.DatabaseConnection.getConnection;
import static io.ylab.tom13.coworkingservice.in.utils.security.PasswordUtil.verifyPassword;

/**
 * Реализация интерфейса {@link UserEditService}.
 * Сервиса для редактирования пользовательских данных.
 */
public class UserEditServiceImpl implements UserEditService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * Конструктор для инициализации сервиса редактирования пользователей.
     */
    public UserEditServiceImpl() {
        try {
            userRepository = new UserRepositoryJdbc(getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
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

        if (userRepository.findById(userToUpdate.id()).isEmpty() || userRepository.findByEmail(userToUpdate.email()).isEmpty() || updatedUser.isEmpty()) {
            throw new RepositoryException("Не удалось обновить пользователя с ID " + userDTO.id());
        }

        return userMapper.toUserDTO(updatedUser.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editPassword(PasswordChangeDTO passwordChangeDTO) throws UnauthorizedException, RepositoryException, UserNotFoundException, UserAlreadyExistsException {

        String email = passwordChangeDTO.email();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("с email " + email));

        checkPassword(passwordChangeDTO.oldPassword(), user.password());

        String newPassword = passwordChangeDTO.newPassword();
        User updatedUser = new User(user.id(), user.firstName(), user.lastName(), user.email(), newPassword, user.role());
        userRepository.updateUser(updatedUser);
    }

    /**
     * Проверяет соответствие пароля из DTO паролю пользователя.
     *
     * @param passwordFromDTO пароль из DTO для проверки.
     * @param passwordFromRep пароль пользователя из репозитория.
     * @throws UnauthorizedException если старый пароль не совпадает с паролем пользователя.
     */
    private void checkPassword(String passwordFromDTO, String passwordFromRep) throws UnauthorizedException {
        if (!verifyPassword(passwordFromDTO, passwordFromRep)) {
            throw new UnauthorizedException("Старый пароль не совпадает");
        }

    }
}

