package io.ylab.tom13.coworkingservice.out;

import io.ylab.tom13.coworkingservice.in.entity.dto.BookingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.entity.model.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.BookingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.CoworkingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.out.menu.MainMenu;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Основной класс приложения CoworkingServiceApplication.
 * Инициализирует сеанс и запускает главное меню.
 */
public class CoworkingServiceApplication {

    /**
     * Конструктор класса CoworkingServiceApplication.
     * Инициализирует сеанс (Session).
     */
    public CoworkingServiceApplication() {
        Session.getInstance();
        dataLoader();
    }

    /**
     * Метод запуска приложения.
     * Создает экземпляр главного меню (MainMenu) и отображает его.
     */
    public void start() {
        new MainMenu().display();
    }

    private void dataLoader() {
        UserRepositoryCollection userRepositoryCollection = UserRepositoryCollection.getInstance();
        CoworkingRepositoryCollection coworkingRepositoryCollection = CoworkingRepositoryCollection.getInstance();
        BookingRepositoryCollection bookingRepositoryCollection = BookingRepositoryCollection.getInstance();
        String testUserMail = "test@mail.ru";
        try {
            userRepositoryCollection.createUser
                    (new RegistrationDTO("Иван", "Иванов", testUserMail,
                            BCrypt.hashpw("pass", BCrypt.gensalt())));
        } catch (UserAlreadyExistsException e) {
            throw new RuntimeException("Ошибка при создании тестового пользователя: " + e.getMessage());
        }
        User user = userRepositoryCollection.findByEmail(testUserMail).get();

        try {
            coworkingRepositoryCollection.createCoworking(
                    new WorkplaceDTO(0, "Офис 1", "Атикафе с кофемашиной и пуфиками", true, "Антикафе"));
            coworkingRepositoryCollection.createCoworking(
                    new WorkplaceDTO(0, "Офис 2", "Рабочее пространство с 3 компьютерами", true, "Офис"));
            coworkingRepositoryCollection.createCoworking(
                    new WorkplaceDTO(0, "Офис 3", "Переговорная комната с большим столом", true, "Переговорная"));
            coworkingRepositoryCollection.createCoworking(
                    new ConferenceRoomDTO(0, "Зал 1", "Большой конференц-зал", true, 300));
            coworkingRepositoryCollection.createCoworking(
                    new ConferenceRoomDTO(0, "Зал 2", "Средний конференц-зал", true, 150));
            coworkingRepositoryCollection.createCoworking(
                    new ConferenceRoomDTO(0, "Зал 3", "Малый конференц-зал", true, 50));
        } catch (CoworkingConflictException e) {
            throw new RuntimeException("Ошибка при заполнении репозитория коворкингов: " + e.getMessage());
        }


        long userId = user.id();
        Map<String, CoworkingDTO> allCoworking = coworkingRepositoryCollection.getAllCoworking();
        List<Long> coworkingIds = allCoworking.values().stream()
                .map(CoworkingDTO::getId)
                .toList();

        LocalDate date = LocalDate.now().plusDays(1);
        List<TimeSlot> timeSlots = List.of(TimeSlot.MORNING, TimeSlot.AFTERNOON);
        try {
            bookingRepositoryCollection.createBooking(new BookingDTO(0, userId, coworkingIds.get(0), date, timeSlots));
            bookingRepositoryCollection.createBooking(new BookingDTO(0, userId, coworkingIds.get(0), date.plusDays(1), timeSlots));
            bookingRepositoryCollection.createBooking(new BookingDTO(0, userId, coworkingIds.get(3), date, timeSlots));
            bookingRepositoryCollection.createBooking(new BookingDTO(0, userId, coworkingIds.get(2), date, timeSlots));
        } catch (BookingConflictException e) {
            throw new RuntimeException("Ошибка при создании тестовых бронирований: " + e.getMessage());
        }
    }
}

