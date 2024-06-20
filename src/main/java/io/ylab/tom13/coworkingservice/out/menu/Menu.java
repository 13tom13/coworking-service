package io.ylab.tom13.coworkingservice.out.menu;

import java.util.Scanner;

/**
 * Базовый класс для меню приложения.
 * Предоставляет методы для считывания данных с консоли.
 */
public abstract class Menu {

    protected static final Scanner scanner = new Scanner(System.in);

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
}

