package nl.saxion.Models.Facade;

import nl.saxion.Models.Manager.PrinterManager;
import nl.saxion.Models.Printers.Printer;
import nl.saxion.Models.Prints.FilamentType;
import nl.saxion.Models.Prints.Print;
import nl.saxion.Models.Prints.PrintTask;
import nl.saxion.Models.Prints.Spool;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PrinterFacade {

    private PrinterManager printerManager = new PrinterManager();

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

    public Print findPrint(int printName) {
        return printerManager.findPrint(printName);
    }

    //TODO: Added method to add PrintTask.
    public void addPrintTask(String printName, List<String> colors, FilamentType chosenType) {
        printerManager.addPrintTask(printName, colors, chosenType);
    }

    //TODO: Added method to add Printer.
    public void addPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, JSONArray currentSpools) {
        printerManager.addPrinter(id, printerType, printerName, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools);
    }

    //TODO: Added method to add Spools.
    public void addSpool(int id, String color, FilamentType filamentType, double length) {
        printerManager.addSpool(id, color, filamentType, length);
    }

    //TODO: Added method to add Prints.
    public void addPrint(String name, String filename, int height, int width, int length, ArrayList<Integer> filamentLength) {
        printerManager.addPrint(name, filename, height, width, length, filamentLength);
    }

    //TODO: Added method to loop through all Filament Types and print them
    public void printAvailableFilamentTypes() {
        int counter = 1;
        for (FilamentType type : FilamentType.values()) {
            System.out.println(counter + ": " + type);
            counter++;
        }
    }

    //TODO: Added method to loop through all Spools and print them. How to return the availableColors list???
    public void printAvailableSpools(FilamentType type) {
        List<String> availableColors = printerManager.getAvailableColors();
        List<Spool> spools = printerManager.getSpools();
        int counter = 1;
        for (var spool : spools) {
            String colorString = spool.getColor();
            if(type == spool.getFilamentType() && !availableColors.contains(colorString)) {
                System.out.println(counter + ": " + colorString + " (" + spool.getFilamentType() + ")");
                printerManager.addAvailableColor(colorString);
                counter++;
            }
        }
    }

    //TODO: Added a method that prints all Spools
    public void printAllSpools() {
        List<Spool> spools =  printerManager.getSpools();
                for (var spool : spools) {
            System.out.println(spool);
        }
    }

    //TODO: Added a method that prints all available Prints
    public void printAvailablePrints() {
        List<Print> prints = printerManager.getAvailablePrints();
        int counter = 1;
        for (var p : prints) {
            System.out.println(counter + ": " + p.getName());
            counter++;
        }
    }

    //TODO: Added a method that prints all pending PrintTasks
    public void printAllPendingPrintTasks() {
        List<PrintTask> printTasks = printerManager.getPendingPrintTasks();
                for (var p : printTasks) {
            System.out.println(p);
        }
    }

    //TODO: Added a method that prints all available Printers
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

    //TODO: Added a method that gets prints all currently running Printers
    public void printCurrentRunningPrinters() {
        List<Printer> printers = printerManager.getPrinters();
        System.out.println("---------- Currently Running Printers ----------");
        for(Printer p: printers) {
            PrintTask printerCurrentTask= printerManager.getPrinterCurrentTask(p);
            if(printerCurrentTask != null) {
                System.out.println(p.getId() + ": " +p.getName() + " - " + printerCurrentTask);
            }
        }
    }

    //TODO: Added a method that registers completion. Currently not working correctly.
    public void registerCompletion(int printerId) {
        printerManager.registerCompletion(printerId);
    }

    //TODO: Added a method that registers failure. Currently not working correctly.
    public void registerFailure(int printerId) {
        printerManager.registerPrinterFailure(printerId);
    }

    //TODO: Added a method that gets all currently running printers
    public List<Printer> getCurrentlyRunningPrinters() {
        List<Printer> printers = printerManager.getPrinters();
        for (Printer p : printers) {
            PrintTask printerCurrentTask = printerManager.getPrinterCurrentTask(p);
            if (printerCurrentTask != null) {
                printers.add(p);
            }
        }
        return printers;
    }

    //TODO: Added a method that gets all available Colors
    public List<String> getAvailableColors() {
        return printerManager.getAvailableColors();
    }

    //TODO: Added a method that gets all available Prints
    public List<Print> getAvailablePrints() {
        return printerManager.getAvailablePrints();
    }

    //TODO: Added a method that gets the Print Strategy
    public String getPrintStrategy() {
        return printerManager.getPrintStrategy();
    }

//    //TODO: Added a method that gets all Spools. Not currently needed.
//    public List<Spool> getAllSpools() {
//        return printerManager.getSpools();
//    }

    //TODO: Added a method that sets the Print Strategy
    public void setPrintStrategy(String strategy) {
        printerManager.setPrintStrategy(strategy);
    }
}
