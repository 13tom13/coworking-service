package io.ylab.tom13.coworkingservice.out.menu.administration.coworking;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.out.client.CoworkingClient;
import io.ylab.tom13.coworkingservice.out.exceptions.CoworkingException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;

public class CoworkingCreateMenu extends Menu {

    private final CoworkingClient coworkingClient;

    public CoworkingCreateMenu() {
        coworkingClient = new CoworkingClient();
    }

    @Override
    public void display() {
        createCoworking();
    }

    private void createCoworking() {
        System.out.println("Выберите тип коворкинга:");
        System.out.println("1. Конференц-зал");
        System.out.println("2. Рабочее место");
        int choice = readInt("Введите номер: ");

        String name = readString("Введите название: ");
        String description = readString("Введите описание: ");
        int availableNumber = readInt("Доступен ли коворкинг? (1.Да / 2.Нет): ");
        boolean available = availableNumber == 1;

        CoworkingDTO coworkingDTO;

        switch (choice) {
            case 1 -> {
                int capacity = readInt("Введите вместимость: ");
                coworkingDTO = new ConferenceRoomDTO(0, name, description, available, capacity);
            }
            case 2 -> {
                String type = readString("Введите тип рабочего места: ");
                coworkingDTO = new WorkplaceDTO(0, name, description, available, type);
            }
            default -> {
                System.err.println("Неверный тип. Попробуйте еще раз.");
                return;
            }
        }

        try {
            coworkingClient.createCoworking(coworkingDTO);
            System.out.println("Коворкинг успешно создан.");
        } catch (CoworkingException e) {
            System.err.println(e.getMessage());
        }
    }
}
