package io.ylab.tom13.coworkingservice.out;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.TimeSlot;
import io.ylab.tom13.coworkingservice.in.entity.model.Booking;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.ConferenceRoom;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Workplace;
import io.ylab.tom13.coworkingservice.in.exceptions.booking.BookingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.BookingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.CoworkingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.out.menu.MainMenu;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.Collection;
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
        User testUser = new User(0, "Иван", "Иванов",
                testUserMail, BCrypt.hashpw("pass", BCrypt.gensalt()), Role.USER);
        User testAdmin = new User(0, "Admin", "Admin", "admin",
                BCrypt.hashpw("admin", BCrypt.gensalt()), Role.ADMINISTRATOR);
        User testModerator = new User(0, "Moderator", "Moderator", "moderator",
                BCrypt.hashpw("moderator", BCrypt.gensalt()), Role.MODERATOR);
        try {
            userRepositoryCollection.createUser(testUser);
            userRepositoryCollection.createUser(testAdmin);
            userRepositoryCollection.createUser(testModerator);
        } catch (RepositoryException e) {
            throw new RuntimeException("Ошибка при создании тестовых пользователей: " + e.getMessage());
        }
        User user = userRepositoryCollection.findByEmail(testUserMail).get();

        try {
            coworkingRepositoryCollection.createCoworking(
                    new Workplace(0, "Офис 1", "Атикафе с кофемашиной и пуфиками", true, "Антикафе"));
            coworkingRepositoryCollection.createCoworking(
                    new Workplace(0, "Офис 2", "Рабочее пространство с 3 компьютерами", true, "Офис"));
            coworkingRepositoryCollection.createCoworking(
                    new Workplace(0, "Офис 3", "Переговорная комната с большим столом", true, "Переговорная"));
            coworkingRepositoryCollection.createCoworking(
                    new ConferenceRoom(0, "Зал 1", "Большой конференц-зал", true, 300));
            coworkingRepositoryCollection.createCoworking(
                    new ConferenceRoom(0, "Зал 2", "Средний конференц-зал", true, 150));
            coworkingRepositoryCollection.createCoworking(
                    new ConferenceRoom(0, "Зал 3", "Малый конференц-зал", true, 50));
        } catch (CoworkingConflictException | RepositoryException e) {
            throw new RuntimeException("Ошибка при заполнении репозитория коворкингов: " + e.getMessage());
        }


        long userId = user.id();
        Collection<Coworking> allCoworking = coworkingRepositoryCollection.getAllCoworking();
        List<Long> coworkingIds = allCoworking.stream()
                .map(Coworking::getId)
                .toList();

        LocalDate date = LocalDate.now().plusDays(1);
        List<TimeSlot> timeSlots = List.of(TimeSlot.MORNING, TimeSlot.AFTERNOON);
        try {

            bookingRepositoryCollection.createBooking(new Booking(0, userId, coworkingIds.get(0), date, timeSlots));
            bookingRepositoryCollection.createBooking(new Booking(0, userId, coworkingIds.get(0), date.plusDays(1), timeSlots));
            bookingRepositoryCollection.createBooking(new Booking(0, userId, coworkingIds.get(3), date, timeSlots));
            bookingRepositoryCollection.createBooking(new Booking(0, userId, coworkingIds.get(2), date, timeSlots));
        } catch (BookingConflictException | RepositoryException e) {
            throw new RuntimeException("Ошибка при создании тестовых бронирований: " + e.getMessage());
        }
    }
}

