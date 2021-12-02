package nl.saxion.Facade;

import nl.saxion.Helper;
import nl.saxion.Manager.PrinterManager;
import nl.saxion.Models.Printers.Printer;
import nl.saxion.Models.Prints.FilamentType;
import nl.saxion.Models.Prints.Print;
import nl.saxion.Models.Prints.PrintTask;
import nl.saxion.Models.Prints.Spool;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PrinterFacade {

    private final PrinterManager printerManager = new PrinterManager();

    public void startInitialQueue() {
        printerManager.startInitialQueue();
    }

    // TODO: Move function to register print completion in menu completely to being automatically updated by Observer?
//    public void registerPrintCompletion() {
//        printerManager.registerPrintCompletion();
//    }

    // TODO: Move function to register print failure in menu completely to being automatically updated by Observer?
//    public void registerPrinterFailure() {
//        printerManager.registerPrinterFailure();
//    }

    public void addNewPrintTask() {
        showItems("Available prints","prints");
        System.out.print("Print number: ");
        Print chosenPrint = findPrintOnInput();

        showItems("Filament Type","filamentType");
        System.out.print("Filament type number: ");
        FilamentType chosenType = findFilamentTypeOnInput();

        printAvailableSpools(chosenType);
        System.out.print("Color number: ");
        List<String> chosenColors = findColorsOnInput(chosenPrint, chosenType);

        addPrintTask(chosenPrint.getName(), chosenColors, chosenType);
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
        String strategy = "";
        if(strategyChoice == 1) {
            strategy = "Less Spool Changes";
        } else if( strategyChoice == 2) {
            strategy = "Efficient Spool Usage";
        }

        printerManager.setPrintStrategy(strategy);
    }

    public Print findPrint(int printName) {
        return printerManager.findPrint(printName);
    }

    public void addPrintTask(String printName, List<String> colors, FilamentType chosenType) {
        printerManager.addPrintTask(printName, colors, chosenType);
    }

    public void addPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, JSONArray currentSpools) {
        printerManager.addPrinter(id, printerType, printerName, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools);
    }

    public void addSpool(int id, String color, FilamentType filamentType, double length) {
        printerManager.addSpool(id, color, filamentType, length);
    }

    public void addPrint(String name, String filename, int height, int width, int length, ArrayList<Integer> filamentLength) {
        printerManager.addPrint(name, filename, height, width, length, filamentLength);
    }

    public void printAvailableFilamentTypes() {
        int counter = 1;
        for (FilamentType type : FilamentType.values()) {
            System.out.println(counter + ": " + type);
            counter++;
        }
    }

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

    public void printAllSpools() {
        List<Spool> spools =  printerManager.getSpools();
                for (var spool : spools) {
            System.out.println(spool);
        }
    }

    public void printAvailablePrints() {
        List<Print> prints = printerManager.getAvailablePrints();
        int counter = 1;
        for (var p : prints) {
            System.out.println(counter + ": " + p.getName());
            counter++;
        }
    }

    public void printAllPendingPrintTasks() {
        List<PrintTask> printTasks = printerManager.getPendingPrintTasks();
                for (var p : printTasks) {
            System.out.println(p);
        }
    }

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

    public void printCurrentRunningPrinters() {
        List<Printer> printers = printerManager.getPrinters();
        for(Printer p: printers) {
            PrintTask printerCurrentTask= printerManager.getPrinterCurrentTask(p);
            if(printerCurrentTask != null) {
                System.out.println(p.getId() + ": " +p.getName() + " - " + printerCurrentTask);
            }
        }
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

    public List<Print> getAvailablePrints() {
        return printerManager.getAvailablePrints();
    }

    public String getPrintStrategy() {
        return printerManager.getPrintStrategy();
    }

    private Print findPrintOnInput() {
        List<Print> prints = getAvailablePrints();
        int printNumber = Helper.numberInput(1, prints.size());
        return findPrint(printNumber - 1);
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
}
