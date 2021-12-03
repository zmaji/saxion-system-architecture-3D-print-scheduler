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

    public void startInitialQueue() {
        printerManager.startInitialQueue();
    }

    /**
     * Print a certain collection with a title.
     * @param title The title displayed above in the terminal.
     * @param type The collection that needs to be displayed.
     */
    public void showItems(String title, String type) {
        System.out.println("-".repeat(10) + " " + title + " " + "-".repeat(10));

        switch (type) {
            case "spools" -> displayManager.printAllSpools();
            case "printers" -> displayManager.printAllPrinters();
            case "prints" -> displayManager.printAvailablePrints();
            case "pendingTasks" -> displayManager.printAllPendingPrintTasks();
            case "filamentType" -> displayManager.printAvailableFilamentTypes();
            case "runningPrinters" -> displayManager.printCurrentRunningPrinters();
        }

        System.out.println("-".repeat(25));
    }

    /**
     * Add a new print task to the queue.
     */
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

    /**
     * Find a print based on the given name.
     * @param printName The name of the print that needs to be found.
     * @return The found Print.
     */
    public Print findPrint(int printName) {
        return printerManager.findPrint(printName);
    }


    public void addPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, JSONArray currentSpools) {
        printerManager.addPrinter(id, printerType, printerName, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools);
    }

    /**
     * Get a collection of all the printers currently running and have a task.
     * @return All the currently running printers.
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

    public void addSpool(int id, String color, FilamentType filamentType, double length) {
        printerManager.addSpool(id, color, filamentType, length);
    }

    public void addPrint(String name, String filename, int height, int width, int length, ArrayList<Integer> filamentLength) {
        printerManager.addPrint(name, filename, height, width, length, filamentLength);
    }

    public List<Print> getAvailablePrints() {
        return printerManager.getAvailablePrints();
    }

    private Print findPrintOnInput() {
        List<Print> prints = getAvailablePrints();
        int printNumber = Helper.numberInput(1, prints.size());
        return findPrint(printNumber - 1);
    }

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

    // This method only changes the name but does not actually work.
    // It exists to demonstrate the output.
    // in the future strategy might be added.
    // TODO: Changed getting and setting the Strategy from Facade + PrinterManager
    public void changePrintStrategy() {
        System.out.println("Current strategy: " + getPrintStrategy());
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

    public PrintStrategy getPrintStrategy() {
        return printerManager.getPrintStrategy();
    }

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
