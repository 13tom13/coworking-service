package io.ylab.tom13.coworkingservice.out.rest.servlet;

import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.BookingService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.BookingServiceImpl;

/**
 * Абстрактный сервлет для бронирования, расширяющий {@link CoworkingServiceServlet}.
 * Содержит базовую функциональность и зависимость от {@link BookingService}.
 */
public abstract class BookingServlet extends CoworkingServiceServlet {
    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     *
     * @param userRepository
     */
    protected BookingServlet(UserRepository userRepository) {
        super(userRepository);
    }

//    /** Сервис для работы с бронированиями */
//    protected final BookingService bookingService;
//
//    /**
//     * Конструктор инициализирует объект {@link BookingService}.
//     */
//    protected BookingServlet() {
//        bookingService = new BookingServiceImpl();
//    }
}
