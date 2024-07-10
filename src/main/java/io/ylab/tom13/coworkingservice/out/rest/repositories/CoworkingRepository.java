package io.ylab.tom13.coworkingservice.out.rest.repositories;


import io.ylab.tom13.coworkingservice.out.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.out.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;

import java.util.Collection;
import java.util.Optional;

/**
 * Репозиторий для работы с коворкингами.
 */
public interface CoworkingRepository {

    /**
     * Получает все коворкинги из репозитория.
     *
     * @return коллекция коворкингов
     * @throws RepositoryException если произошла ошибка доступа к репозиторию
     */
    Collection<Coworking> getAllCoworking() throws RepositoryException;

    /**
     * Получает коворкинг по его идентификатору.
     *
     * @param coworkingId идентификатор коворкинга
     * @return объект Optional, содержащий коворкинг, если он найден, иначе пустой Optional
     * @throws CoworkingNotFoundException если коворкинг с указанным идентификатором не найден
     * @throws RepositoryException если произошла ошибка доступа к репозиторию
     */
    Optional<Coworking> getCoworkingById(long coworkingId) throws CoworkingNotFoundException, RepositoryException;

    /**
     * Создает новый коворкинг на основе переданных данных.
     *
     * @param coworkingDTO данные для создания коворкинга
     * @return объект Optional, содержащий созданный коворкинг, если успешно создан, иначе пустой Optional
     * @throws CoworkingConflictException если конфликтует с уже существующим коворкингом
     * @throws RepositoryException если произошла ошибка доступа к репозиторию
     */
    Optional<Coworking> createCoworking(Coworking coworkingDTO) throws CoworkingConflictException, RepositoryException;

    /**
     * Удаляет коворкинг по его идентификатору.
     *
     * @param coworkingId идентификатор коворкинга для удаления
     * @throws CoworkingNotFoundException если коворкинг с указанным идентификатором не найден
     * @throws RepositoryException если произошла ошибка доступа к репозиторию
     */
    void deleteCoworking(long coworkingId) throws CoworkingNotFoundException, RepositoryException;

    /**
     * Обновляет информацию о коворкинге на основе переданных данных.
     *
     * @param coworkingDTO данные для обновления коворкинга
     * @return объект Optional, содержащий обновленный коворкинг, если успешно обновлен, иначе пустой Optional
     * @throws CoworkingNotFoundException если коворкинг с указанным идентификатором не найден
     * @throws CoworkingConflictException если конфликтует с другим коворкингом при обновлении
     * @throws RepositoryException если произошла ошибка доступа к репозиторию
     */
    Optional<Coworking> updateCoworking(Coworking coworkingDTO) throws CoworkingNotFoundException, CoworkingConflictException, RepositoryException;
}
