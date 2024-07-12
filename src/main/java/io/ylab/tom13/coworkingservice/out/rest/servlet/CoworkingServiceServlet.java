package io.ylab.tom13.coworkingservice.out.rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.security.SecurityHTTPController;

/**
 * Абстрактный сервлет, предоставляющий общую функциональность и константы для сервлетов обслуживания коворкинга.
 * Расширяет {@link SecurityHTTPController} для обеспечения функциональности безопасности.
 */
public abstract class CoworkingServiceServlet extends SecurityHTTPController {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected CoworkingServiceServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /** Константа для параметра идентификатора бронирования */
//    public static final String BOOKING_ID = "bookingId";
//
//    /** Константа для параметра идентификатора пользователя */
//    public static final String USER_ID = "userId";
//
//    /** Константа для параметра идентификатора коворкинга */
//    public static final String COWORKING_ID = "coworkingId";
//
//    /** Константа для параметра email */
//    public static final String EMAIL = "email";
//
//    /** Константа для параметра даты */
//    public static final String DATE = "date";
//
//    /** Константа для типа содержимого (application/json) */
//    public static final String CONTENT_TYPE = "application/json";
//
//    /** Константа для кодировки (UTF-8) */
//    public static final String ENCODING = "UTF-8";
//
//    /** Объект ObjectMapper для сериализации и десериализации JSON */
//    protected final ObjectMapper objectMapper;
//
//    /** Локальная сессия для управления сеансом */
//    protected final Session localSession = Session.getInstance();
//
//    /**
//     * Конструктор инициализирует объект ObjectMapper с использованием метода getObjectMapper().
//     */
//    public CoworkingServiceServlet() {
//        objectMapper = getObjectMapper();
//    }
//
//    /**
//     * Метод для чтения JSON-запроса из HttpServletRequest.
//     *
//     * @param request HTTP-запрос
//     * @return JSON-строка запроса
//     * @throws IOException если возникает ошибка при чтении запроса
//     */
//    protected String getJsonRequest(HttpServletRequest request) throws IOException {
//        BufferedReader reader = request.getReader();
//        StringBuilder jsonRequestBuilder = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            jsonRequestBuilder.append(line);
//        }
//        return jsonRequestBuilder.toString();
//    }
//
//    /**
//     * Метод для установки JSON-ответа в HttpServletResponse.
//     *
//     * @param response     HTTP-ответ
//     * @param responseBody объект, который нужно сериализовать в JSON и отправить
//     * @throws IOException если возникает ошибка при записи ответа
//     */
//    protected void setJsonResponse(HttpServletResponse response, Object responseBody) throws IOException {
//        response.setContentType(CONTENT_TYPE);
//        response.setCharacterEncoding(ENCODING);
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
//    }
//
//    /**
//     * Метод для установки JSON-ответа с определенным HTTP-статусом в HttpServletResponse.
//     *
//     * @param response     HTTP-ответ
//     * @param responseBody объект, который нужно сериализовать в JSON и отправить
//     * @param status       HTTP-статус ответа
//     * @throws IOException если возникает ошибка при записи ответа
//     */
//    protected void setJsonResponse(HttpServletResponse response, Object responseBody, int status) throws IOException {
//        response.setContentType(CONTENT_TYPE);
//        response.setCharacterEncoding(ENCODING);
//        response.setStatus(status);
//        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
//    }
//
//    /**
//     * Метод для преобразования строки параметра в Optional<Long>.
//     *
//     * @param param строковое значение параметра
//     * @return Optional<Long> с числовым значением параметра, если успешно преобразовано, иначе Optional.empty()
//     */
//    protected static Optional<Long> getLongParam(String param) {
//        long longParam;
//        try {
//            longParam = Long.parseLong(param);
//        } catch (NumberFormatException e) {
//            return Optional.empty();
//        }
//        return Optional.of(longParam);
//    }
}