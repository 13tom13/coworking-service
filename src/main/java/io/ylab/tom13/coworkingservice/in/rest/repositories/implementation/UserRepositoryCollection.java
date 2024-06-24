package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Реализация репозитория пользователей с использованием коллекций.
 */
public class UserRepositoryCollection implements UserRepository {

    private static final UserRepositoryCollection INSTANCE = new UserRepositoryCollection();

    private UserRepositoryCollection() {
    }

    /**
     * Получение единственного экземпляра класса репозитория.
     *
     * @return экземпляр UserRepositoryCollection
     */
    public static UserRepositoryCollection getInstance() {
        return INSTANCE;
    }

    private final Map<Long, User> usersById = new HashMap<>();
    private final Map<String, Long> emailToIdMap = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    private final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO createUser(RegistrationDTO registrationDTO) throws UserAlreadyExistsException {
        String email = registrationDTO.email();
        if (emailToIdMap.containsKey(email)) {
            throw new UserAlreadyExistsException(email);
        }
        long id = idCounter.incrementAndGet();
        User newUser = new User(id, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(), registrationDTO.password(), registrationDTO.role());
        usersById.put(id, newUser);
        emailToIdMap.put(email, id);
        return userMapper.toUserDTO(newUser);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByEmail(String email) {
        Long id = emailToIdMap.get(email);
        return id != null ? Optional.ofNullable(usersById.get(id)) : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(usersById.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserById(long id) throws UserNotFoundException {
        User user = usersById.remove(id);
        if (user == null) {
            throw new UserNotFoundException("с ID " + id);
        }
        emailToIdMap.remove(user.email());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO updateUser(UserDTO userDTO) throws UserNotFoundException, UserAlreadyExistsException {
        long id = userDTO.id();

        if (!usersById.containsKey(id)) {
            throw new UserNotFoundException("с ID " + id);
        }

        User existingUser = usersById.get(id);
        String newEmail = userDTO.email();
        String existingEmail = existingUser.email();

        if (!newEmail.equals(existingEmail) && emailToIdMap.containsKey(newEmail)) {
            throw new UserAlreadyExistsException(newEmail);
        }

        User updatedUser = userMapper.toUser(userDTO, id);

        usersById.put(id, updatedUser);

        if (!newEmail.equals(existingEmail)) {
            emailToIdMap.remove(existingEmail);
            emailToIdMap.put(newEmail, id);
        }

        return userMapper.toUserDTO(updatedUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<User> getAllUsers() {
        return usersById.values();
    }

    @Override
    public void updatePassword(PasswordChangeDTO passwordChangeDTO) throws UserNotFoundException, UnauthorizedException {
        String email  = passwordChangeDTO.email();
        User user = findByEmail(email).orElseThrow(() -> new UserNotFoundException("с email " + email));
        if (BCrypt.checkpw(passwordChangeDTO.oldPassword(),user.password())){
            String newPassword = passwordChangeDTO.newPassword();
            User updatedUser = new User(user.id(), user.firstName(), user.lastName(), user.email(), newPassword, user.role());
            usersById.put(user.id(), updatedUser);
        } else {
            throw new UnauthorizedException();
        }
    }
}

