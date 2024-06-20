package io.ylab.tom13.coworkingservice.out;

import java.util.Scanner;

public abstract class Menu {

    protected static final Scanner scanner = new Scanner(System.in);

    public abstract void display();

    // Метод для чтения строки с консоли
    protected String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    protected int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный выбор. Пожалуйста, введите корректное число.");
            }
        }
    }
}
