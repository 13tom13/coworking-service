package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;
import org.springframework.http.ResponseEntity;

/**
 * Контроллер для управления операциями коворкингов.
 */
public interface CoworkingController {

    /**
     * Получение всех коворкингов.
     *
     * @return ResponseEntity с картой всех коворкингов, где ключ - имя коворкинга, значение - DTO коворкинга.
     */
    ResponseEntity<?> getAllCoworking();

    /**
     * Получение всех доступных коворкингов.
     *
     * @return ResponseEntity с картой всех доступных коворкингов, где ключ - имя коворкинга, значение - DTO коворкинга.
     */
    ResponseEntity<?> getAllAvailableCoworkings();

    /**
     * Создание нового коворкинга.
     *
     * @param coworkingDTO DTO с данными для создания коворкинга.
     * @return ResponseEntity с информацией о созданном коворкинге или сообщением об ошибке.
     */
    ResponseEntity<?> createCoworking(CoworkingDTO coworkingDTO);

    /**
     * Обновление информации о коворкинге.
     *
     * @param coworkingDTO DTO с данными для обновления коворкинга.
     * @return ResponseEntity с обновленными данными коворкинга или сообщением об ошибке.
     */
    ResponseEntity<?> updateCoworking(CoworkingDTO coworkingDTO);

    /**
     * Удаление коворкинга по ID.
     *
     * @param coworkingId ID коворкинга для удаления.
     * @return ResponseEntity с сообщением об успешном удалении или ошибке.
     */
    ResponseEntity<?> deleteCoworking(long coworkingId);
}

