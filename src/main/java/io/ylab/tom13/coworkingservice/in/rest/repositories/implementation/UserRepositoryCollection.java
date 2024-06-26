package io.ylab.tom13.coworkingservice.in.rest.repositories.implementation;

import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
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
    public Optional<User> createUser(User user) throws RepositoryException {
        if (emailToIdMap.containsKey(user.email())){
            throw new RepositoryException("Пользователь с email уже существует");
        }
        User userForDB = new User(idCounter.incrementAndGet(), user.firstName(), user.lastName(), user.email(), user.password(), user.role());

        usersById.put(userForDB.id(), userForDB);
        emailToIdMap.put(userForDB.email(), userForDB.id());

        if (!emailToIdMap.containsKey(userForDB.email())) {
            return Optional.empty();
        }
        return Optional.ofNullable(usersById.get(userForDB.id()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<User> getAllUsers() {
        return usersById.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByEmail(String email) {
        Long id = emailToIdMap.get(email);
        return Optional.ofNullable(usersById.get(id)) ;
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
    public Optional<User> updateUser(User user) throws RepositoryException {
        long id = user.id();
        String userEmail = usersById.get(id).email();
        if (userEmail == null){
            throw new RepositoryException("Пользователь не найден");
        }
        usersById.put(id, user);

        String newEmail = user.email();

        if (!newEmail.equals(userEmail)) {
            emailToIdMap.remove(userEmail);
            emailToIdMap.put(newEmail, id);
            userEmail = newEmail;
        }
        if (!usersById.containsKey(user.id()) || !emailToIdMap.containsKey(userEmail)) {
            throw new RepositoryException("Не удалось обновить пользователя с email: " + userEmail);
        }
        return Optional.ofNullable(usersById.get(user.id()));
    }

}

