package util;

import java.util.Scanner;

/** Reads and validates what the user types in the console. */
public class InputHelper {

    private final Scanner scanner = new Scanner(System.in);

    /** Reads one line. Stops the program cleanly if the input stream is closed. */
    public String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("Input closed");
        }
        return scanner.nextLine().trim();
    }

    /** Keeps asking until the user types something (not empty). */
    public String readRequired(String prompt) {
        while (true) {
            String text = readLine(prompt);
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("This field cannot be empty. Please try again.");
        }
    }

    /** Keeps asking until the user types a whole number. */
    public int readInt(String prompt) {
        while (true) {
            String text = readLine(prompt);
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    /** Same as readInt, but the number must be inside a range. */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            int number = readInt(prompt);
            if (number >= min && number <= max) {
                return number;
            }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    /** Keeps asking until the user types a decimal number inside a range. */
    public double readDouble(String prompt, double min, double max) {
        while (true) {
            String text = readLine(prompt);
            try {
                double number = Double.parseDouble(text);
                if (number >= min && number <= max) {
                    return number;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public boolean confirm(String prompt) {
        while (true) {
            String answer = readLine(prompt + " (y/n): ").toLowerCase();
            if (answer.equals("y")) {
                return true;
            }
            if (answer.equals("n")) {
                return false;
            }
            System.out.println("Please type y or n.");
        }
    }
}
