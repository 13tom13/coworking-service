package io.ylab.tom13.coworkingservice.out.rest.services.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.repositories.implementation.UserRepositoryJdbc;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import io.ylab.tom13.coworkingservice.out.utils.mapper.UserMapper;

import java.sql.SQLException;
import java.util.Optional;

import static io.ylab.tom13.coworkingservice.out.database.DatabaseConnection.getConnection;

/**
 * Реализация интерфейса {@link RegistrationService}.
 * Сервиса регистрации пользователей.
 */
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;

    public final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * Конструктор для инициализации сервиса регистрации с использованием репозитория пользователей.
     */
    public RegistrationServiceImpl() {
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
    public UserDTO createUser(RegistrationDTO registrationDTO) throws UserAlreadyExistsException, RepositoryException {
        String email = registrationDTO.email();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }

        User newUser = new User(0, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(), registrationDTO.password(), Role.USER);
        Optional<User> userFromRep = userRepository.createUser(newUser);
        if (userFromRep.isEmpty()) {
            throw new RepositoryException("Не удалось создать пользователя");
        }
        return userMapper.toUserDTO(userFromRep.get());
    }
}

