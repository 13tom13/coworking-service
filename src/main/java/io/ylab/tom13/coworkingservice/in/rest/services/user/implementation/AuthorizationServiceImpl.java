package io.ylab.tom13.coworkingservice.in.rest.services.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.user.AuthorizationService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

/**
 * Реализация сервиса авторизации пользователей.
 */
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;

    /**
     * Конструктор для инициализации сервиса авторизации с использованием репозитория пользователей.
     */
    public AuthorizationServiceImpl() {
        userRepository = UserRepositoryCollection.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO login(AuthorizationDTO authorizationDTO) throws UnauthorizedException {
        String email = authorizationDTO.email();
        String passwordFromDTO = authorizationDTO.password();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (authenticateUser(passwordFromDTO, user.getPassword())) {
                return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * Аутентификация пользователя сравнивает предоставленный пароль с сохраненным хэшированным паролем.
     *
     * @param passwordFromDTO пароль, предоставленный для аутентификации
     * @param passwordFromRepository хэшированный пароль пользователя из хранилища
     * @return true, если пароли совпадают, иначе false
     */
    private boolean authenticateUser(String passwordFromDTO, String passwordFromRepository) {
        return BCrypt.checkpw(passwordFromDTO, passwordFromRepository);
    }
}

