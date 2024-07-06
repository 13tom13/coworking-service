package io.ylab.tom13.coworkingservice.out.rest.services;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;

/**
 * Сервис для редактирования пользовательских данных.
 */
public interface UserEditService {

    /**
     * Редактирует данные пользователя.
     *
     * @param userDTO DTO с данными пользователя для редактирования.
     * @return обновленный DTO пользователя.
     * @throws RepositoryException        если возникает ошибка репозитория при доступе к данным.
     * @throws UserNotFoundException      если пользователь с указанным ID не найден.
     * @throws UserAlreadyExistsException если пользователь с указанным email уже существует.
     */
    UserDTO editUser(UserDTO userDTO) throws RepositoryException, UserNotFoundException, UserAlreadyExistsException;

    /**
     * Изменяет пароль пользователя.
     *
     * @param passwordChangeDTO DTO с данными для изменения пароля.
     * @throws UnauthorizedException если текущий пользователь не авторизован для выполнения операции.
     * @throws RepositoryException   если возникает ошибка репозитория при доступе к данным.
     * @throws UserNotFoundException если пользователь с указанным ID не найден.
     */
    void editPassword(PasswordChangeDTO passwordChangeDTO) throws UnauthorizedException, RepositoryException, UserNotFoundException, UserAlreadyExistsException;
}
