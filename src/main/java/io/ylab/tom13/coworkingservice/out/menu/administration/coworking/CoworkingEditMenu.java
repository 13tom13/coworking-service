package io.ylab.tom13.coworkingservice.out.menu.administration.coworking;

import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.ConferenceRoomDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.CoworkingDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.coworking.WorkplaceDTO;
import io.ylab.tom13.coworkingservice.out.client.CoworkingClient;
import io.ylab.tom13.coworkingservice.out.exceptions.CoworkingException;
import io.ylab.tom13.coworkingservice.out.menu.Menu;
import io.ylab.tom13.coworkingservice.out.utils.Session;

public class CoworkingEditMenu extends Menu {

    private final CoworkingClient coworkingClient;

    public CoworkingEditMenu() {
        coworkingClient = new CoworkingClient();
    }

    @Override
    public void display() {
        boolean editMenu = true;
        while (editMenu) {
            CoworkingDTO coworkingDTO = (CoworkingDTO) localSession.getAttribute("editableCoworking");
            System.out.println("Меню редактирования коворкинга:");
            String available = coworkingDTO.isAvailable() ? "(Доступно)" : "(Недоступно)";
            System.out.println(available + " " + coworkingDTO);
            System.out.println("Выберите действие:");
            System.out.println("1. Изменить название коворкинга");
            System.out.println("2. Изменить описание коворкинга");
            if (coworkingDTO instanceof WorkplaceDTO) {
                System.out.println("3. изменить тип коворкинга");
            }
            if (coworkingDTO instanceof ConferenceRoomDTO) {
                System.out.println("3. изменить вместимость коворкинга");
            }
            System.out.println("4. Изменить доступность коворкинга");
            System.out.println("5. Удалить коворкинг");
            System.out.println("0. Выход из редактирования коворкинга");
            System.out.println();
            int choice = readInt("Введите номер действия: ");
            switch (choice) {
                case 1 -> editingCoworkingName(coworkingDTO);
                case 2 -> editingDescription(coworkingDTO);
                case 3 -> {
                    if (coworkingDTO instanceof WorkplaceDTO workplaceDTO) {
                        editingType(workplaceDTO);
                    }
                    if (coworkingDTO instanceof ConferenceRoomDTO conferenceRoomDTO) {
                        editCapacity(conferenceRoomDTO);
                    }
                }
                case 4 -> editingAvailability(coworkingDTO);
                case 5 -> {
                    deleteCoworking(coworkingDTO);
                    localSession.removeAttribute("editableCoworking");
                    editMenu = false;
                }
                case 0 -> {
                    System.err.println("Выход из меню редактирования бронирования");
                    localSession.removeAttribute("editableCoworking");
                    editMenu = false;
                }
                default -> System.err.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    private void editingCoworkingName(CoworkingDTO coworkingDTO) {
        System.out.println(coworkingDTO);
        String newName = readString("Введите новое название коворкинга: ");
        coworkingDTO.setName(newName);
        updateCoworking(coworkingDTO);
    }

    private void editingDescription(CoworkingDTO coworkingDTO) {
        System.out.println(coworkingDTO);
        String newName = readString("Введите новое описание коворкинга: ");
        coworkingDTO.setDescription(newName);
        updateCoworking(coworkingDTO);
    }

    private void editingType(WorkplaceDTO workplaceDTO) {
        System.out.println(workplaceDTO);
        String newType = readString("Введите новый тип коворкинга: ");
        workplaceDTO.setType(newType);
        updateCoworking(workplaceDTO);
    }

    private void editCapacity(ConferenceRoomDTO conferenceRoomDTO) {
        System.out.println(conferenceRoomDTO);
        int newCapacity = readInt("Введите новое значение вместимости коворкинга: ");
        conferenceRoomDTO.setCapacity(newCapacity);
        updateCoworking(conferenceRoomDTO);
    }

    private void editingAvailability(CoworkingDTO coworkingDTO) {
        coworkingDTO.setAvailable(!coworkingDTO.isAvailable());
        updateCoworking(coworkingDTO);
    }

    private void deleteCoworking(CoworkingDTO coworkingDTO) {
        System.out.println(coworkingDTO);
        System.out.println("Вы уверены, что хотите удалить коворкинг?");
        System.out.println("1. да");
        System.out.println("2. нет");
        int choice = readInt("Введите номер действия: ");
        switch (choice) {
            case 1 -> {
                try {
                    coworkingClient.deleteBooking(coworkingDTO.getId());
                    System.err.println("Коворкинг успешно удалено.");
                } catch (CoworkingException e) {
                    System.err.println(e.getMessage());
                }
            }
            case 2 -> System.err.println("Удаление коворкинга отменено.");
        }
    }


    private void updateCoworking(CoworkingDTO coworkingDTO) {
        try {
            CoworkingDTO updateCoworking = coworkingClient.updateCoworking(coworkingDTO);
            localSession.setAttribute("editableCoworking", updateCoworking);
            System.out.println("Коворкинг успешно изменен");
        } catch (CoworkingException e) {
            System.err.println(e.getMessage());
        }
    }
}
