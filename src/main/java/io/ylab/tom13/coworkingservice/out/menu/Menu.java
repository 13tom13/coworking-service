package io.ylab.tom13.coworkingservice.out.menu;

import io.ylab.tom13.coworkingservice.out.utils.Session;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static io.ylab.tom13.coworkingservice.in.utils.security.PasswordUtil.hashPassword;

/**
 * Базовый класс для меню приложения.
 * Предоставляет методы для считывания данных с консоли.
 */
public abstract class Menu {

    protected static final Scanner scanner = new Scanner(System.in);

    protected final Session localSession = Session.getInstance();

    /**
     * Абстрактный метод, который должен быть реализован в подклассах для отображения меню.
     */
    public abstract void display();

    /**
     * Считывает строку с консоли, предварительно выводя заданный приглашающий текст.
     *
     * @param prompt Поясняющий текст для пользователя.
     * @return Введенная пользователем строка (без начальных и конечных пробелов).
     */
    protected String readString(String prompt) {
        String input;
        do {
            System.out.println(prompt);
            input = scanner.nextLine().trim(); // Удаляем лишние пробелы по краям
            if (input.isEmpty()) {
                System.err.println("Неверный ввод. Ввод не должен быть пустым.");
            }
        } while (input.isEmpty());

        return input;
    }

    /**
     * Считывает целое число с консоли, предварительно выводя заданный приглашающий текст.
     *
     * @param prompt Поясняющий текст для пользователя.
     * @return Введенное пользователем целое число.
     */
    protected int readInt(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Неверный ввод. Пожалуйста, введите корректное число.");
            }
        }
    }

    /**
     * Считывает целое число с консоли, предварительно выводя заданный приглашающий текст.
     *
     * @param prompt Поясняющий текст для пользователя.
     * @return Введенное пользователем целое число.
     */
    protected long readLong(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Неверный ввод. Пожалуйста, введите корректное число.");
            }
        }
    }

    /**
     * Считывает дату с консоли в формате "ДД.ММ.ГГ", предварительно выводя заданный приглашающий текст.
     *
     * @return Введенная пользователем дата в формате LocalDate.
     */
    protected LocalDate readLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        while (true) {
            System.out.println("Введите дату в формате ДД.ММ.ГГ:");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.err.println("Неверный формат даты. Пожалуйста, введите дату в формате ДД.ММ.ГГ.");
            }
        }
    }

    protected String readPassword(String prompt) {
        String password = readString(prompt);
        return hashPassword(password);
    }


    protected LocalDate correctDate() {
        while (true) {
            LocalDate localDate = readLocalDate();
            if (localDate.isAfter(LocalDate.now())) {
                return localDate;
            } else {
                System.err.println("Дата должна быть в будущем. Пожалуйста, введите корректную дату.");
            }
        }
    }

}

