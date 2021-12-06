package nl.saxion.models.manager;

import nl.saxion.models.factories.PrintFactory;
import nl.saxion.models.factories.PrinterFactory;
import nl.saxion.models.factories.SpoolFactory;
import nl.saxion.models.printers.HousedPrinter;
import nl.saxion.models.printers.MultiColor;
import nl.saxion.models.printers.Printer;
import nl.saxion.models.printers.StandardFDM;
import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.Print;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;
import nl.saxion.models.readers.PrintReader;
import nl.saxion.models.readers.PrinterReader;
import nl.saxion.models.readers.ReaderException;
import nl.saxion.models.readers.SpoolReader;
import nl.saxion.models.strategies.LessSpoolChangeStrategy;
import nl.saxion.models.strategies.PrintStrategy;

import java.util.*;

public class PrinterManager {
    private List<Printer> printers = new ArrayList<>();
    private List<Print> prints = new ArrayList<>();
    private List<Spool> spools = new ArrayList<>();

    private List<Spool> freeSpools = new ArrayList<>(); // TODO: Decide if this should be used at all.
    private List<Printer> freePrinters = new ArrayList<>();
    private List<PrintTask> pendingPrintTasks = new ArrayList<>();
    private HashMap<Printer, PrintTask> runningPrintTasks = new HashMap();

    private PrintStrategy printStrategy = new LessSpoolChangeStrategy();
    private PrinterFactory printerFactory = new PrinterFactory(this);
    private PrintFactory printFactory = new PrintFactory(this);
    private SpoolFactory spoolFactory = new SpoolFactory(this);

    private PrinterReader printerReader;
    private PrintReader printReader;
    private SpoolReader spoolReader;

    public PrinterManager() {
        this.printReader = new PrintReader("/prints.json", this.printFactory);
        this.spoolReader = new SpoolReader("/spools.json", this.spoolFactory);
        this.printerReader = new PrinterReader("/printers.json", this.printerFactory);
        try {
            printReader.readPrintsFromFile();
            spoolReader.readPrintsFromFile();
            printerReader.readPrintsFromFile();
        } catch (ReaderException e) {
            e.printStackTrace();
        }
    }

    /** Checks if the color of a given spool matches the name
     *
     * @param list a list to search in
     * @param name a name to compare to
     * @return a list with all found matches
     */
    public boolean containsSpool(final List<Spool> list, final String name){
        return list.stream().anyMatch(o -> o.getColor().equals(name));
    }

    public void selectPrintTask(Printer printer) {
        Spool[] spools = printer.getCurrentSpools();
        PrintTask chosenTask = null;
        // First we look if there's a task that matches the current spool on the printer.
        for(PrintTask printTask: pendingPrintTasks) {
            if(printer.printFits(printTask.getPrint())) {
                if (printer instanceof StandardFDM && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1) {
                    if (spools[0].spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                        runningPrintTasks.put(printer, printTask);
                        freePrinters.remove(printer);
                        chosenTask = printTask;
                        break;
                    }
                    // The housed printer is the only one that can print ABS, but it can also print the others.
                } else if (printer instanceof HousedPrinter && printTask.getColors().size() == 1) {
                    if (spools[0].spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                        runningPrintTasks.put(printer, printTask);
                        freePrinters.remove(printer);
                        chosenTask = printTask;
                        break;
                    }
                    // For multicolor the order of spools does matter, so they have to match.
                } else if (printer instanceof MultiColor && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() <= ((MultiColor) printer).getMaxColors()) {
                    boolean printWorks = true;
                    for (int i = 0; i < spools.length && i < printTask.getColors().size(); i++) {
                        if (!spools[i].spoolMatch(printTask.getColors().get(i), printTask.getFilamentType())) {
                            printWorks = false;
                        }
                    }
                    if (printWorks) {
                        runningPrintTasks.put(printer, printTask);
                        freePrinters.remove(printer);
                        chosenTask = printTask;
                        break;
                    }
                }
            }
        }
        if(chosenTask != null) {
            pendingPrintTasks.remove(chosenTask);
            System.out.println("Started task " + chosenTask + " on printer " + printer.getName());
        } else {
            // If we didn't find a print for the current spool we search for a print with the free spools.
            for(PrintTask printTask: pendingPrintTasks) {
                if(printer.printFits(printTask.getPrint()) && getPrinterCurrentTask(printer) == null) {
                    if (printer instanceof StandardFDM && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1) {
                        Spool chosenSpool = null;
                        for (Spool spool : freeSpools) {
                            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                                chosenSpool = spool;
                            }
                        }
                        if (chosenSpool != null) {
                            runningPrintTasks.put(printer, printTask);
                            freeSpools.add(printer.getCurrentSpools()[0]);
                            System.out.println("Please place spool " + chosenSpool.getId() + " in printer " + printer.getName());
                            freeSpools.remove(chosenSpool);
                            freePrinters.remove(printer);
                            chosenTask = printTask;
                        }
                    } else if (printer instanceof HousedPrinter && printTask.getColors().size() == 1) {
                        Spool chosenSpool = null;
                        for (Spool spool : freeSpools) {
                            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                                chosenSpool = spool;
                            }
                        }
                        if (chosenSpool != null) {
                            runningPrintTasks.put(printer, printTask);
                            freeSpools.add(printer.getCurrentSpools()[0]);
                            System.out.println("Please place spool " + chosenSpool.getId() + " in printer " + printer.getName());
                            freeSpools.remove(chosenSpool);
                            freePrinters.remove(printer);
                            chosenTask = printTask;
                        }
                    } else if (printer instanceof MultiColor && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() <= ((MultiColor) printer).getMaxColors()) {
                        ArrayList<Spool> chosenSpools = new ArrayList<>();
                        for (int i = 0; i < printTask.getColors().size(); i++) {
                            for (Spool spool : freeSpools) {
                                if (spool.spoolMatch(printTask.getColors().get(i), printTask.getFilamentType()) && !containsSpool(chosenSpools, printTask.getColors().get(i))) {
                                    chosenSpools.add(spool);
                                }
                            }
                        }
                        // We assume that if they are the same length that there is a match.
                        if (chosenSpools.size() == printTask.getColors().size()) {
                            runningPrintTasks.put(printer, printTask);

                            freeSpools.addAll(Arrays.asList(printer.getCurrentSpools()));

                            printer.setCurrentSpools(chosenSpools);
                            for (Spool spool : chosenSpools) {
                                System.out.println("Please place spool " + spool.getId() + " in printer " + printer.getName());
                                freeSpools.remove(spool);
                            }
                            freePrinters.remove(printer);
                            chosenTask = printTask;
                        }
                    }
                }
            }
            if(chosenTask != null) {
                pendingPrintTasks.remove(chosenTask);
                System.out.println("Started task " + chosenTask + " on printer " + printer.getName());
            }
        }
    }

    /** Loops through a List of Printers and selects a Print Task for every element */
    public void startInitialQueue() {
        for(Printer printer: printers) {
            selectPrintTask(printer);
        }
    }

    public void addToPrintList(Print print) {
        prints.add(print);
    }

    public void addSpool(Spool spool) {
        spools.add(spool);
    }

    /** Gets the current Print Task of a given Printer
     *
     * @param printer the Printer to get the current Print Task of
     * @return the Print Task value of the Printer index out of the HashMap
     */
    public PrintTask getPrinterCurrentTask(Printer printer) {
        if(!runningPrintTasks.containsKey(printer)) {
            return null;
        }
        return runningPrintTasks.get(printer);
    }

    /** Gets a list of all pending Print Tasks
     *
     * @return the list of all pending Print Tasks
     */
    public List<PrintTask> getPendingPrintTasks() {
        return pendingPrintTasks;
    }

    /** Adds a Print Task based on given parameters
     *
     * @param printName the name of the Print to add to a Task
     * @param colors the list of colors to be used
     * @param type the FilamentType to be used
     */
    public void addPrintTask(String printName, List<String> colors, FilamentType type) {
        Print print = findPrint(printName);
        if (print == null) {
            printError("Could not find print with name " + printName);
            return;
        }
        if (colors.size() == 0) {
            printError("Need at least one color, but none given");
            return;
        }
        for (String color : colors) {
            boolean found = false;
            for (Spool spool : spools) {
                if (spool.getColor().equals(color) && spool.getFilamentType() == type) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                printError("Color " + color + " (" + type +") not found");
                return;
            }
        }

        PrintTask task = new PrintTask(print, colors, type);
        pendingPrintTasks.add(task);
        System.out.println("Added task to queue");
    }

    /** Finds a Print based on a given name
     *
     * @param printName the name of the Print to look for
     * @return the found Print
     */
    public Print findPrint(String printName) {
        for (Print p : prints) {
            if (p.getName().equals(printName)) {
                return p;
            }
        }
        return null;
    }

    /** Finds a Print based on a given index
     *
     * @param index the index to look at
     * @return the found Print
     */
    public Print findPrint(int index) {
        if(index > prints.size() -1) {
            return null;
        }
        return prints.get(index);
    }

    /** Finds a Spool based on a given ID
     *
     * @param id the ID to look for
     * @return the found Spool
     */
    public Spool getSpoolByID(int id) {
        for(Spool s: spools) {
            if(s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public void registerPrinterFailure(int printerId) {
        Map.Entry<Printer, PrintTask> foundEntry = null;
        for (Map.Entry<Printer, PrintTask> entry : runningPrintTasks.entrySet()) {
            if (entry.getKey().getId() == printerId) {
                foundEntry = entry;
                break;
            }
        }
        if (foundEntry == null) {
            printError("cannot find a running task on printer with ID " + printerId);
            return;
        }
        PrintTask task = foundEntry.getValue();
        pendingPrintTasks.add(task); // add the task back to the queue.
        runningPrintTasks.remove(foundEntry.getKey());

        System.out.println("Task " + task + " removed from printer "
                + foundEntry.getKey().getName());

        Printer printer = foundEntry.getKey();
        Spool[] spools = printer.getCurrentSpools();
        for(int i=0; i<spools.length && i < task.getColors().size();i++) {
            spools[i].reduceLength(task.getPrint().getFilamentLength().get(i));
        }
    }

    public void registerCompletion(int printerId) {
        Map.Entry<Printer, PrintTask> foundEntry = null;
        for (Map.Entry<Printer, PrintTask> entry : runningPrintTasks.entrySet()) {
            if (entry.getKey().getId() == printerId) {
                foundEntry = entry;
                break;
            }
        }
        if (foundEntry == null) {
            printError("cannot find a running task on printer with ID " + printerId);
            return;
        }
        PrintTask task = foundEntry.getValue();
        runningPrintTasks.remove(foundEntry.getKey());

        System.out.println("Task " + task + " removed from printer "
                + foundEntry.getKey().getName());

        Printer printer = foundEntry.getKey();
        Spool[] spools = printer.getCurrentSpools();
        for(int i=0; i<spools.length && i < task.getColors().size();i++) {
            spools[i].reduceLength(task.getPrint().getFilamentLength().get(i));
        }
        selectPrintTask(printer);


    }

    /** Prints an Error based on given String
     *
     * @param s the String to print
     */
    private void printError(String s) {
        System.out.println("Error: "+s);
        System.out.println("Press Enter to continue");
        new Scanner(System.in).nextLine();
    }

    public PrintStrategy getPrintStrategy() {
        return printStrategy;
    }
    public List<Print> getAvailablePrints() {
        return prints;
    }
    public List<Spool> getSpools() {
        return spools;
    }
    public List<Printer> getPrinters() {
        return printers;
    }

    public void setPrintStrategy(PrintStrategy printStrategy) {
        this.printStrategy = printStrategy;
    }

    public void addFreePrinter(Printer printer) {
        freePrinters.add(printer);
    }

    public void addToPrinters(Printer printer) {
        printers.add(printer);
    }

    public void removeFreeSpool(Spool spool) {
        freeSpools.remove(spool);
    }

}
