package io.ylab.tom13.coworkingservice.out.rest.controller;

import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.coworking.CoworkingDTO;

import java.util.Map;

/**
 * Контроллер для управления операциями коворкингов.
 */
public interface CoworkingController {

    /**
     * Получение всех коворкингов.
     *
     * @return ResponseDTO с картой всех коворкингов, где ключ - имя коворкинга, значение - DTO коворкинга.
     */
    ResponseDTO<Map<String, CoworkingDTO>> getAllCoworking();

    /**
     * Получение всех доступных коворкингов.
     *
     * @return ResponseDTO с картой всех доступных коворкингов, где ключ - имя коворкинга, значение - DTO коворкинга.
     */
    ResponseDTO<Map<String, CoworkingDTO>> getAllAvailableCoworkings();

    /**
     * Создание нового коворкинга.
     *
     * @param coworkingDTO DTO с данными для создания коворкинга.
     * @return ResponseDTO с информацией о созданном коворкинге или сообщением об ошибке.
     */
    ResponseDTO<CoworkingDTO> createCoworking(CoworkingDTO coworkingDTO);

    /**
     * Обновление информации о коворкинге.
     *
     * @param coworkingDTO DTO с данными для обновления коворкинга.
     * @return ResponseDTO с обновленными данными коворкинга или сообщением об ошибке.
     */
    ResponseDTO<CoworkingDTO> updateCoworking(CoworkingDTO coworkingDTO);

    /**
     * Удаление коворкинга по ID.
     *
     * @param coworkingId ID коворкинга для удаления.
     * @return ResponseDTO с сообщением об успешном удалении или ошибке.
     */
    ResponseDTO<Void> deleteCoworking(long coworkingId);
}

