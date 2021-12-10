package nl.saxion.models.facade;

import nl.saxion.Helper;
import nl.saxion.models.managers.DisplayManager;
import nl.saxion.models.managers.PrinterManager;
import nl.saxion.models.printers.Printer;
import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.Print;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;
import nl.saxion.models.strategies.EfficientSpoolUsageStrategy;
import nl.saxion.models.strategies.LessSpoolChangeStrategy;
import nl.saxion.models.strategies.PrintStrategy;

import java.util.ArrayList;
import java.util.List;

public class PrinterFacade {

    private final PrinterManager printerManager = new PrinterManager();
    private final DisplayManager displayManager = new DisplayManager(printerManager);

    /** Uses PrinterManager to start the initial Print queue */
    public void startInitialQueue() {
        printerManager.startInitialQueue();
    }

    /**
     * Shows items based on a certain type to display and a title
     * @param title title to print in front of the list
     * @param type type of item to show (spools, printers, prints, pendingTasks, filamentType, runningPrinters)
     */
    public void showItems(String title, String type) {
        System.out.println("-".repeat(10) + " " + title + " " + "-".repeat(10));

        switch (type) {
            case "spools" -> displayManager.printAllSpools();
            case "printers" -> displayManager.printAllPrinters();
            case "detailedPrints" ->displayManager. printAllPrintsFull();
            case "prints" ->displayManager. printAvailablePrints();
            case "pendingTasks" -> displayManager.printAllPendingPrintTasks();
            case "filamentType" -> displayManager.printAvailableFilamentTypes();
            case "runningPrinters" -> displayManager.printCurrentRunningPrinters();
        }

        System.out.println("-".repeat(22+title.length()));
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
}
