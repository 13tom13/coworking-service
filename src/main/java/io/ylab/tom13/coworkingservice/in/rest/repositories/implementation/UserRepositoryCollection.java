package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;

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
        User user = new User(id, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(), registrationDTO.password());
        usersById.put(id, user);
        emailToIdMap.put(email, id);
        return new UserDTO(id, user.getFirstName(), user.getLastName(), user.getEmail());
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
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
        emailToIdMap.remove(user.getEmail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUser(User user) throws UserNotFoundException, UserAlreadyExistsException {
        long id = user.getId();
        if (!usersById.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
        String newEmail = user.getEmail();
        String existingEmail = usersById.get(id).getEmail();
        if (!newEmail.equals(existingEmail) && emailToIdMap.containsKey(newEmail)) {
            throw new UserAlreadyExistsException("Пользователь с email " + newEmail + " уже существует");
        }

        usersById.put(id, user);

        if (!newEmail.equals(existingEmail)) {
            emailToIdMap.remove(existingEmail);
            emailToIdMap.put(newEmail, id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<User> getAllUsers() {
        return usersById.values();
    }
}

