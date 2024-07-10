package io.ylab.tom13.coworkingservice.out.rest.services;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingUpdatingExceptions;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;

import java.util.Map;

/**
 * Сервис для управления коворкингами.
 */
public interface CoworkingService {

    /**
     * Получает все коворкинги.
     *
     * @return Map с информацией о всех коворкингах.
     */
    Map<String, CoworkingDTO> getAllCoworking() throws RepositoryException;

    /**
     * Получает все доступные коворкинги.
     *
     * @return Map с информацией о всех доступных коворкингах.
     */
    Map<String, CoworkingDTO> getAllAvailableCoworkings() throws RepositoryException;

    /**
     * Создает новый коворкинг.
     *
     * @param coworkingDTO DTO с данными для создания коворкинга.
     * @return созданный DTO коворкинга.
     * @throws CoworkingConflictException если возникает конфликт при создании коворкинга.
     * @throws RepositoryException        если возникает ошибка репозитория при доступе к данным.
     */
    CoworkingDTO createCoworking(CoworkingDTO coworkingDTO) throws CoworkingConflictException, RepositoryException;

    /**
     * Обновляет информацию о коворкинге.
     *
     * @param coworkingDTO DTO с данными для обновления коворкинга.
     * @return обновленный DTO коворкинга.
     * @throws CoworkingUpdatingExceptions если возникает ошибка при обновлении коворкинга.
     * @throws CoworkingNotFoundException  если коворкинг с указанным ID не найден.
     * @throws CoworkingConflictException  если возникает конфликт при обновлении коворкинга.
     */
    CoworkingDTO updateCoworking(CoworkingDTO coworkingDTO) throws CoworkingUpdatingExceptions, CoworkingNotFoundException, CoworkingConflictException, RepositoryException;

    /**
     * Удаляет коворкинг по ID.
     *
     * @param coworkingId ID коворкинга для удаления.
     * @throws CoworkingNotFoundException если коворкинг с указанным ID не найден.
     */
    void deleteCoworking(long coworkingId) throws CoworkingNotFoundException, RepositoryException;
}
