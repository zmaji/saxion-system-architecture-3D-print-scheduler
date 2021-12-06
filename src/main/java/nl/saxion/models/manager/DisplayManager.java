package nl.saxion.models.manager;
import nl.saxion.models.printers.Printer;
import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.Print;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;

import java.util.ArrayList;
import java.util.List;

public class DisplayManager {
    private final PrinterManager printerManager;

    public DisplayManager(nl.saxion.models.manager.PrinterManager printerManager) {
        this.printerManager = printerManager;
    }

    /** Prints all Spools based on a List that is kept by PrinterManager */
    public void printAllSpools() {
        List<Spool> spools =  printerManager.getSpools();
        for (var spool : spools) {
            System.out.println(spool);
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

        System.out.println("-".repeat(10) + " Spools " + "-".repeat(10));
        for (var spool : spools) {
            String colorString = spool.getColor();
            if(type == spool.getFilamentType() && !availableColors.contains(colorString)) {
                System.out.println(counter + ": " + colorString + " (" + spool.getFilamentType() + ")");
                availableColors.add(colorString);
                counter++;
            }
        }

        System.out.println("-".repeat(22+"Spools".length()));

    }

    /** Prints all available Prints based on a List that is kept by PrinterManager */
    public void printAllPrintsFull() {
        List<Print> prints = printerManager.getAvailablePrints();
        for (var p : prints) {
            System.out.println(p.toString());
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

    /** Prints a list of all currently available Filament Type values */
    public void printAvailableFilamentTypes() {
        int counter = 1;
        for (FilamentType type : FilamentType.values()) {
            System.out.println(counter + ": " + type);
            counter++;
        }
    }
}
