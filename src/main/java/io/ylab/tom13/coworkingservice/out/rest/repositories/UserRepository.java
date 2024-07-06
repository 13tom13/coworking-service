package io.ylab.tom13.coworkingservice.out.rest.repositories;

import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс репозитория пользователей.
 * Определяет методы для работы с пользователями в хранилище.
 */
public interface UserRepository {

    /**
     * Добавление нового пользователя в репозиторий.
     * Пользователи должны быть уникальными по email.
     *
     * @param user пользователь для добавления.
     * @return UserDTO данные добавленного пользователя.
     * @throws RepositoryException ошибка репозитория.
     * @throws UserAlreadyExistsException если пользователь с таким email уже существует.
     */
    Optional<User> createUser(User user) throws RepositoryException, UserAlreadyExistsException;

    /**
     * Поиск пользователя по email.
     *
     * @param email Email пользователя для поиска.
     * @return Optional с найденным пользователем или Optional.empty(), если пользователь не найден.
     */
    Optional<User> findByEmail(String email);

    /**
     * Поиск пользователя по id.
     *
     * @param id ID пользователя для поиска.
     * @return Optional с найденным пользователем или Optional.empty(), если пользователь не найден.
     */
    Optional<User> findById(long id);

    /**
     * Удаление пользователя из репозитория по id.
     *
     * @param id ID пользователя для удаления.
     * @throws UserNotFoundException если пользователь с указанным ID не найден.
     * @throws RepositoryException ошибка репозитория.
     */
    void deleteUserById(long id) throws UserNotFoundException, RepositoryException;

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user объект пользователя для обновления
     * @throws UserNotFoundException      если пользователь с указанным ID не найден.
     * @throws UserAlreadyExistsException если найден пользователь с таким же email.
     * @throws RepositoryException ошибка репозитория.
     */
    Optional<User> updateUser(User user) throws RepositoryException, UserAlreadyExistsException, UserNotFoundException;

    /**
     * Получение всех пользователей из репозитория.
     *
     * @return Коллекция всех пользователей.
     */
    Collection<User> getAllUsers();

}

