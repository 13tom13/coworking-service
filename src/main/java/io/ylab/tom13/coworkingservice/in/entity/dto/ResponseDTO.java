package io.ylab.tom13.coworkingservice.in.entity.dto;

/**
 * Общий DTO для ответа, который может содержать данные и сообщение об успехе или ошибке операции.
 *
 * @param <T> тип данных, содержащихся в ответе
 */
public record ResponseDTO<T>(
        boolean success,
        T data,
        String message
) {
    /**
     * Создает успешный ответ с данными.
     *
     * @param <T>  тип данных в ответе
     * @param data данные, возвращаемые в ответе
     * @return объект ResponseDTO, представляющий успешный ответ
     */
    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(true, data, null);
    }

    /**
     * Создает ответ об ошибке с указанным сообщением.
     *
     * @param <T>     тип данных в ответе
     * @param message сообщение об ошибке
     * @return объект ResponseDTO, представляющий ответ об ошибке
     */
    public static <T> ResponseDTO<T> failure(String message) {
        return new ResponseDTO<>(false, null, message);
    }
}
