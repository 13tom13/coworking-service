package io.ylab.tom13.coworkingservice.out.menu.administration.coworking;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.out.client.CoworkingClient;
import io.ylab.tom13.coworkingservice.out.menu.Menu;

import java.util.Map;

public class CoworkingAdministrationMenu extends Menu {

    private final CoworkingClient coworkingClient;

    private final CoworkingCreateMenu coworkingCreateMenu;
    private final CoworkingEditMenu coworkingEditMenu;

    public CoworkingAdministrationMenu() {
        coworkingClient = new CoworkingClient();
        coworkingCreateMenu = new CoworkingCreateMenu();
        coworkingEditMenu = new CoworkingEditMenu();
    }

    @Override
    public void display() {
        boolean startMenu = true;
        while (startMenu) {
            Map<String, CoworkingDTO> coworkings = coworkingClient.getAllCoworkings();
            localSession.setAttribute("allCoworkings", coworkings);
            System.out.println("Меню работы с коворкингами");
            System.out.println("Выберите действие:");
            System.out.println("1. Просмотр всех коворкингов");
            System.out.println("2. Создание коворкинга");
            System.out.println("3. Редактирование коворкинга");
            System.out.println("0. Выход");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> viewAllCoworkings(coworkings);
                case 2 -> coworkingCreateMenu.display();
                case 3 -> getCoworkingForEdit(coworkings);
                case 0 -> {
                    System.err.println("Выход из меню бронирования");
                    startMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    private void viewAllCoworkings(Map<String, CoworkingDTO> coworkings) {
        System.out.println("Список коворкингов:");
        coworkings.values().forEach(coworkingDTO -> {
            String available = coworkingDTO.isAvailable() ? "(Доступно)" : "(Недоступно)";
            System.out.println(available + " " + coworkingDTO);
        });
        System.out.println();
    }

    private void getCoworkingForEdit(Map<String, CoworkingDTO> coworkings) {
        viewAllCoworkings(coworkings);
        String coworkingName = readString("Введите название коворкинга для редактирования:");

        if (!coworkings.containsKey(coworkingName)) {
            System.err.println("Коворкинг с таким названием не найден.");
            return;
        }

        CoworkingDTO coworkingDTO = coworkings.get(coworkingName);
        localSession.setAttribute("editableCoworking", coworkingDTO);
        coworkingEditMenu.display();
    }
}
