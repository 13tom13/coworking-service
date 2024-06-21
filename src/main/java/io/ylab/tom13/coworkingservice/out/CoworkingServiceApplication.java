package io.ylab.tom13.coworkingservice.out;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.space.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.CoworkingRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.out.menu.MainMenu;
import io.ylab.tom13.coworkingservice.out.utils.Session;
import org.mindrot.jbcrypt.BCrypt;

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
        try {
            userRepositoryCollection.createUser
                    (new RegistrationDTO("Иван", "Иванов","test@mail.ru",
                            BCrypt.hashpw("pass", BCrypt.gensalt())));
        } catch (UserAlreadyExistsException e) {
            throw new RuntimeException("Ошибка при создании тестового пользователя");
        }
        CoworkingRepositoryCollection coworkingRepositoryCollection = CoworkingRepositoryCollection.getInstance();
        coworkingRepositoryCollection.addWorkplace(
                new WorkplaceDTO(0,"Офис 13", "Атикафе с кофемашиной и пуфиками", true,"Антикафе"));
        coworkingRepositoryCollection.addConferenceRoom(
                new ConferenceRoomDTO(0,"Зал 1",  "Большой конференц-зал", true,300));
    }
}

