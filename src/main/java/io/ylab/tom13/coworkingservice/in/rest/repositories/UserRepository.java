package io.ylab.tom13.coworkingservice.in.rest.repositories;

import io.ylab.tom13.coworkingservice.in.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;

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
     * @param registrationDTO DTO с данными пользователя для добавления.
     * @throws UserAlreadyExistsException если пользователь с таким email уже существует.
     * @return UserDTO данные добавленного пользователя.
     */
    UserDTO createUser(RegistrationDTO registrationDTO) throws UserAlreadyExistsException;

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
     */
    void deleteUserById(long id) throws UserNotFoundException;

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user объект пользователя для обновления
     * @throws UserNotFoundException если пользователь с указанным ID не найден.
     * @throws UserAlreadyExistsException если найден пользователь с таким же email.
     */
    UserDTO updateUser(UserDTO user) throws UserNotFoundException, UserAlreadyExistsException;

    /**
     * Получение всех пользователей из репозитория.
     *
     * @return Коллекция всех пользователей.
     */
    Collection<User> getAllUsers();

    void updatePassword(PasswordChangeDTO passwordChangeDTO) throws UserNotFoundException, UnauthorizedException;
}

