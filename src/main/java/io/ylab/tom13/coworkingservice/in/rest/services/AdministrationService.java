package io.ylab.tom13.coworkingservice.in.rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;

import java.util.List;

/**
 * Сервис для администрирования пользователей.
 */
public interface AdministrationService {

    /**
     * Получение списка всех пользователей.
     *
     * @return список всех пользователей в виде списка DTO.
     */
    List<UserDTO> getAllUsers();

    /**
     * Получение пользователя по электронной почте.
     *
     * @param email электронная почта пользователя для поиска.
     * @return DTO пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     */
    UserDTO getUserByEmail(String email) throws UserNotFoundException;

    /**
     * Редактирование пользователя администратором.
     *
     * @param userDTO DTO с информацией для редактирования пользователя.
     * @return отредактированный DTO пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws RepositoryException   если произошла ошибка репозитория при сохранении данных.
     */
    UserDTO editUserByAdministrator(UserDTO userDTO) throws UserNotFoundException, RepositoryException, UserAlreadyExistsException;

    /**
     * Изменение пароля пользователя администратором.
     *
     * @param userId          ID пользователя для изменения пароля.
     * @param newHashPassword новый хешированный пароль пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws RepositoryException   если произошла ошибка репозитория при сохранении данных.
     */
    void editUserPasswordByAdministrator(long userId, String newHashPassword) throws UserNotFoundException, RepositoryException, UserAlreadyExistsException;

    /**
     * Регистрация нового пользователя администратором.
     *
     * @param registrationDTO DTO с данными для регистрации нового пользователя.
     * @param role            роль, которую получит новый пользователь.
     * @throws RepositoryException если произошла ошибка репозитория при сохранении данных.
     */
    void registrationUser(RegistrationDTO registrationDTO, Role role) throws RepositoryException, UserAlreadyExistsException;
}
