package nl.saxion;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Helper {
    private static final Scanner scanner = new Scanner(System.in);

    /** Gives a printline based on certain given user input
     *
     * @param max maximum value for a user to choose from
     * @return the value of the choice
     */
    public static int menuChoice(int max) {
        int choice = -1;
        while (choice < 0 || choice > max) {
            System.out.print("Choose an option: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                //try again after consuming the current line
                System.out.println("Error: Invalid input");
                scanner.nextLine();
            }
        }
        return choice;
    }

    public static String stringInput() {
        String input = null;
        while(input == null || input.length() == 0){
            input = scanner.nextLine();
        }
        return input;
    }

    public static int numberInput() {
        return scanner.nextInt();
    }

    public static int numberInput(int min, int max) {
        int input = numberInput();
        while (input < min || input > max) {
            input = numberInput();
        }
        return input;
    }

    public static Scanner getScanner() {
        return scanner;
    }
}
