package io.ylab.tom13.coworkingservice.in.rest.services.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.user.RegistrationService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;

/**
 * Реализация сервиса регистрации пользователей.
 */
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;

    public final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * Конструктор для инициализации сервиса регистрации с использованием репозитория пользователей.
     */
    public RegistrationServiceImpl() {
        userRepository = UserRepositoryCollection.getInstance();
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

        User newUser = new User(0, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(), registrationDTO.password(), registrationDTO.role());
        User userFromRep = userRepository.createUser(newUser);
        return userMapper.toUserDTO(userFromRep);
    }
}

