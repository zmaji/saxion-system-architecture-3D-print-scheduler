package nl.saxion;

import nl.saxion.models.facade.PrinterFacade;
import java.util.*;

public class Main {
    private final PrinterFacade printerFacade = new PrinterFacade();

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        int choice = 1;
        while (choice > 0 && choice < 10) {
            // Print menu
            menu();
            choice = menuChoice(9);
            if (choice == 1) {
                // Add new Print Task
                printerFacade.addNewPrintTask();
            } else if (choice == 2) {
                // Regiser a completed Print
                printerFacade.registerPrintCompletion();
            } else if (choice == 3) {
                // Register a failed Print
                printerFacade.registerPrinterFailure();
            } else if (choice == 4) {
                // Change the current Print Strategy
                printerFacade.changePrintStrategy();
            } else if (choice == 5) {
                // Start the Print queue
                printerFacade.startInitialQueue();
            } else if (choice == 6) {
                // Display all available Prints
                printerFacade.showItems("Available prints", "detailedPrints");
            } else if (choice == 7) {
                // Display all available Printers
                printerFacade.showItems("Available printers", "printers");
            } else if (choice == 8) {
                // Display all Spools
                printerFacade.showItems("Spools", "spools");
            } else if (choice == 9) {
                // Display all pending Print Tasks
                printerFacade.showItems("Pending Print Tasks", "pendingTasks");
            }
        }
    }

    /** Prints menu with all available options for user to choose from **/
    public void menu() {
        System.out.println("Print Manager");
        System.out.println("=============");
        System.out.println("1) Add new Print Task");
        System.out.println("2) Register Printer Completion");
        System.out.println("3) Register Printer Failure");
        System.out.println("4) Change printing style");
        System.out.println("5) Start Print Queue");
        System.out.println("6) Show prints");
        System.out.println("7) Show printers");
        System.out.println("8) Show spools");
        System.out.println("9) Show pending print tasks");
        System.out.println("0) Exit");
    }

    /** Gives a printline based on certain given user input
     *
     * @param max maximum value for a user to choose from
     * @return the value of the choice
     */
    public int menuChoice(int max) {
        int choice = -1;
        while (choice < 0 || choice > max) {
            System.out.print("Choose an option: ");
            try {
                choice = Helper.getScanner().nextInt();
            } catch (InputMismatchException e) {
                //try again after consuming the current line
                System.out.println("Error: Invalid input");
                Helper.getScanner().nextLine();
            }
        }
        return choice;
    }

}
