package nl.saxion.Facade;

import nl.saxion.Helper;
import nl.saxion.Manager.DisplayManager;
import nl.saxion.Manager.PrinterManager;
import nl.saxion.Models.Printers.Printer;
import nl.saxion.Models.Prints.FilamentType;
import nl.saxion.Models.Prints.Print;
import nl.saxion.Models.Prints.PrintTask;
import nl.saxion.Models.Prints.Spool;
import nl.saxion.strategies.EfficientSpoolUsageStrategy;
import nl.saxion.strategies.LessSpoolChangeStrategy;
import nl.saxion.strategies.PrintStrategy;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PrinterFacade {

    private final PrinterManager printerManager = new PrinterManager();
    private final DisplayManager displayManager = new DisplayManager(printerManager);

    /** Uses PrinterManager to start the initial Print queue */
    public void startInitialQueue() {
        printerManager.startInitialQueue();
    }

    /** Asks user for input to give PrinterManager three arguments to add a PrintTask: Print, FilamentType and a List of colors */
    public void addNewPrintTask() {
        showItems("Available prints","prints");
        System.out.print("Print number: ");
        Print chosenPrint = findPrintOnInput();

        showItems("Filament Type","filamentType");
        System.out.print("Filament type number: ");
        FilamentType chosenType = findFilamentTypeOnInput();

        displayManager.printAvailableSpools(chosenType);
        System.out.print("Color number: ");
        List<String> chosenColors = findColorsOnInput(chosenPrint, chosenType);

        printerManager.addPrintTask(chosenPrint.getName(), chosenColors, chosenType);
    }

   /** Sets the Strategy Type based on user input */
    public void changePrintStrategy() {
        System.out.println("Current strategy: " + printerManager.getPrintStrategy());
        System.out.println("1: Less Spool Changes");
        System.out.println("2: Efficient Spool Usage");
        System.out.println("Choose strategy: ");
        int strategyChoice = Helper.numberInput(1, 2);
        PrintStrategy strategy = null;
        if(strategyChoice == 1) {
            strategy = new LessSpoolChangeStrategy();
        } else if( strategyChoice == 2) {
            strategy = new EfficientSpoolUsageStrategy();
        }

        System.out.println("Strategy set to: " + strategy.toString());
        printerManager.setPrintStrategy(strategy);
    }

    //TODO: Only used in Reader, could potentially be directly used from PrinterManager
    /**
     * Uses the PrinterManager to add a Printer based on given parameters
     *
     * @param id the ID value of the Printer
     * @param printerType the Type value of the Printer
     * @param printerName the name of the Printer
     * @param manufacturer the manufacturer of the Printer
     * @param maxX the maxX value of the Printer
     * @param maxY the maxY value of the Printer
     * @param maxZ the maxZ value of the Printer
     * @param maxColors the maximum colors of the Printer
     * @param currentSpools the currentSpools of the Printer
     */
    public void addPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, JSONArray currentSpools) {
        printerManager.addPrinter(id, printerType, printerName, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools);
    }

    //TODO: Only used in Reader, could potentially be directly used from PrinterManager
    /**
     * Uses the PrinterManager to add a Spool based on given parameters
     *
     * @param id the ID value of the Spool
     * @param color the Color of the Spool
     * @param filamentType the FilamentType of the Spool
     * @param length the length of the Spool
     */
    public void addSpool(int id, String color, FilamentType filamentType, double length) {
        printerManager.addSpool(id, color, filamentType, length);
    }

    //TODO: Only used in Reader, could potentially be directly used from PrinterManager
    /**
     * Uses the PrinterManager to add a Print based on given parameters
     *
     * @param name the name of the Print
     * @param filename the filename of the Print
     * @param height the height of the Print
     * @param width the width of the Print
     * @param length the length of the Print
     * @param filamentLength the filamentLength of the Print
     */
    public void addPrint(String name, String filename, int height, int width, int length, ArrayList<Integer> filamentLength) {
        printerManager.addPrint(name, filename, height, width, length, filamentLength);
    }

    /** Prints a list of all currently available Filament Type values */
    public void printAvailableFilamentTypes() {
        int counter = 1;
        for (FilamentType type : FilamentType.values()) {
            System.out.println(counter + ": " + type);
            counter++;
        }
    }

    /** Prints a list of all available Spools based on a given Filament Type
     *
     * @param type the FilamentType to get the available Spools of
     */
    public void printAvailableSpools(FilamentType type) {
        List<String> availableColors = new ArrayList<>();
        List<Spool> spools = printerManager.getSpools();
        int counter = 1;
        for (var spool : spools) {
            String colorString = spool.getColor();
            if(type == spool.getFilamentType() && !availableColors.contains(colorString)) {
                System.out.println(counter + ": " + colorString + " (" + spool.getFilamentType() + ")");
                availableColors.add(colorString);
                counter++;
            }
        }
    }

    /** Gets all available colors based on a given Filament Type
     *
     * @param type the FilamentType to get the available colors of
     * @return List of available colors
     */
    private List<String> getAvailableColors(FilamentType type) {
        List<String> availableColors = new ArrayList<>();
        List<Spool> spools = printerManager.getSpools();
        for (var spool : spools) {
            String colorString = spool.getColor();
            if(type == spool.getFilamentType() && !availableColors.contains(colorString)) {
                availableColors.add(colorString);
            }
        }

        return availableColors;
    }

    /** Shows items based on a certain type to display and a title
     *
     * @param title title to print in front of the list
     * @param type type of item to show (spools, printers, prints, pendingTasks, filamentType, runningPrinters)
     */
    public void showItems(String title, String type) {
        System.out.println("-".repeat(10) + " " + title + " " + "-".repeat(10));

        switch (type) {
            case "spools" -> printAllSpools();
            case "printers" -> printAllPrinters();
            case "prints" -> printAvailablePrints();
            case "pendingTasks" -> printAllPendingPrintTasks();
            case "filamentType" -> printAvailableFilamentTypes();
            case "runningPrinters" -> printCurrentRunningPrinters();
        }

        System.out.println("-".repeat(25));
    }

    /** Prints all Spools based on a List that is kept by PrinterManager */
    public void printAllSpools() {
        List<Spool> spools =  printerManager.getSpools();
                for (var spool : spools) {
            System.out.println(spool);
        }
    }

    /** Prints all available Prints based on a List that is kept by PrinterManager */
    public void printAvailablePrints() {
        List<Print> prints = printerManager.getAvailablePrints();
        int counter = 1;
        for (var p : prints) {
            System.out.println(counter + ": " + p.getName());
            counter++;
        }
    }

    /** Prints all pending Print Tasks based on a list that is kept by PrinterManager */
    public void printAllPendingPrintTasks() {
        List<PrintTask> printTasks = printerManager.getPendingPrintTasks();
                for (var p : printTasks) {
            System.out.println(p);
        }
    }

    /** Prints all Printers including their current Print Task based on a list that is kept by PrinterManager */
    public void printAllPrinters() {
        List<Printer> printers = printerManager.getPrinters();
                for (var p : printers) {
            System.out.print(p);
            PrintTask currentTask = printerManager.getPrinterCurrentTask(p);
            if(currentTask != null) {
                System.out.println("Current Print Task: " + currentTask);
            }
            System.out.println();
        }
    }

    /** Prints all currently running Printers based on if they have a PrintTask */
    public void printCurrentRunningPrinters() {
        List<Printer> printers = printerManager.getPrinters();
        for(Printer p: printers) {
            PrintTask printerCurrentTask= printerManager.getPrinterCurrentTask(p);
            if(printerCurrentTask != null) {
                System.out.println(p.getId() + ": " +p.getName() + " - " + printerCurrentTask);
            }
        }
    }

    /** Registers the State of a PrintTask being completed based on user input */
    public void registerPrintCompletion() {
        showItems("Currently Running Printers", "runningPrinters");

        System.out.print("Printer that is done (ID): ");
        int printerId = Helper.numberInput(1, getCurrentlyRunningPrinters().size());
        printerManager.registerCompletion(printerId);
    }

    /** Registers the State of a PrintTask being failed based on user input */
    public void registerPrinterFailure() {
        showItems("Currently Running Printers", "runningPrinters");

        System.out.print("Printer ID that failed: ");
        int printerId = Helper.numberInput(1, getCurrentlyRunningPrinters().size());
        printerManager.registerPrinterFailure(printerId);
    }

    /** Gets all currently running Printers that have a PrintTask based on a list kept by PrinterManager
     *
     * @return the list of all running Printers
     */
    public List<Printer> getCurrentlyRunningPrinters() {
        List<Printer> printers = printerManager.getPrinters();
        List<Printer> runningPrinters = new ArrayList<>();
        for (Printer p : printers) {
            PrintTask printerCurrentTask = printerManager.getPrinterCurrentTask(p);
            if (printerCurrentTask != null) {
                runningPrinters.add(p);
            }
        }
        return runningPrinters;
    }

    /** Finds a Print based on printNumber given by user input
     *
     * @return the found Print
     */
    private Print findPrintOnInput() {
        List<Print> prints = printerManager.getAvailablePrints();
        int printNumber = Helper.numberInput(1, prints.size());
        return printerManager.findPrint(printNumber - 1);
    }

    /** Selects a FilamentType based on user input
     *
     * @return the selected FilamentType
     */
    private FilamentType findFilamentTypeOnInput() {
        int filamentType = Helper.numberInput(1, 3);
        FilamentType type = null;
        switch (filamentType) {
            case 1 -> type = FilamentType.PLA;
            case 2 -> type = FilamentType.PETG;
            case 3 -> type = FilamentType.ABS;
            default -> System.out.println("Not a valid filamentType, bailing out");
        }
        return type;
    }

    /** Finds available colors based on a Print and FilamentType for a user to choose from
     *
     * @param print the Print to get the filamentLength of
     * @param type the type to get the available colors of
     * @return a list of chosen colors
     */
    private List<String> findColorsOnInput(Print print, FilamentType type) {
        List<String> colors = new ArrayList<>();
        List<String> availableColors = getAvailableColors(type);

        int colorChoice = Helper.numberInput(1, availableColors.size()); // Keep in mind that 0, is starting entry for a list.
        colors.add(availableColors.get(colorChoice-1));
        for(int i = 1; i < print.getFilamentLength().size(); i++) {
            System.out.print("Color number: ");
            colorChoice = Helper.numberInput(1, availableColors.size());
            colors.add(availableColors.get(colorChoice-1));
        }
        return colors;
    }

    //TODO: Move function to register print failure in menu completely to being automatically updated by Observer?
    public void registerPrintCompletion() {
        showItems("Currently Running Printers", "runningPrinters");

        System.out.print("Printer that is done (ID): ");
        int printerId = Helper.numberInput(1, getCurrentlyRunningPrinters().size());
        printerManager.registerCompletion(printerId);
    }

    //TODO: Move function to register print failure in menu completely to being automatically updated by Observer?
    public void registerPrinterFailure() {
        showItems("Currently Running Printers", "runningPrinters");

        System.out.print("Printer ID that failed: ");
        int printerId = Helper.numberInput(1, getCurrentlyRunningPrinters().size());
        printerManager.registerPrinterFailure(printerId);
    }
}
