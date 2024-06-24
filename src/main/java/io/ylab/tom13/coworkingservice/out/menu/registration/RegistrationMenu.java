package io.ylab.tom13.coworkingservice.out.menu.registration;


import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.client.RegistrationClient;
import io.ylab.tom13.coworkingservice.out.exceptions.RegistrationException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Меню регистрации пользователя.
 * Предоставляет интерфейс для регистрации нового пользователя через взаимодействие с консолью.
 */
public class RegistrationMenu extends Menu {

    private final RegistrationClient registrationClient;

    /**
     * Конструктор, инициализирующий клиент для регистрации.
     */
    public RegistrationMenu() {
        this.registrationClient = new RegistrationClient();
    }

    /**
     * Отображает меню регистрации пользователя.
     * Вызывает метод регистрации пользователя через клиент.
     */
    @Override
    public void display() {
            registration();
    }

    /**
     * Метод для выполнения процесса регистрации пользователя.
     * Считывает данные пользователя с консоли и передает их клиенту для регистрации.
     */
    private void registration() {
        String firstName = readString("Введите имя пользователя:");
        String lastName = readString("Введите фамилию:");
        String email = readString("Введите email:");
        String hashPassword = BCrypt.hashpw(readString("Введите пароль:"), BCrypt.gensalt());
        RegistrationDTO registrationDTO = new RegistrationDTO(firstName, lastName, email, hashPassword, Role.USER);
        try {
            registrationClient.registration(registrationDTO);
        } catch (RegistrationException e) {
            System.err.println(e.getMessage());
        }
    }
}

