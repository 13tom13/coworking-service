package io.ylab.tom13.coworkingservice.out.rest.services.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
import io.ylab.tom13.coworkingservice.out.utils.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.ylab.tom13.coworkingservice.out.security.PasswordUtil.verifyPassword;

/**
 * Реализация интерфейса {@link AuthorizationService}.
 * Сервиса авторизации пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {


    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO login(AuthorizationDTO authorizationDTO) {
        String email = authorizationDTO.email();
        String passwordFromDTO = authorizationDTO.password();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (authenticateUser(passwordFromDTO, user.password())) {
                return userMapper.toUserDTO(user);
            } else {
                throw new UnauthorizedException("Неверный email или пароль");
            }
        } else {
            throw new UnauthorizedException("Неверный email или пароль");
        }
    }

    /**
     * Аутентификация пользователя сравнивает предоставленный пароль с сохраненным хэшированным паролем.
     *
     * @param passwordFromDTO        пароль, предоставленный для аутентификации
     * @param passwordFromRepository хэшированный пароль пользователя из хранилища
     * @return true, если пароли совпадают, иначе false
     */
    private boolean authenticateUser(String passwordFromDTO, String passwordFromRepository) {
        return verifyPassword(passwordFromDTO, passwordFromRepository);

    }
}

