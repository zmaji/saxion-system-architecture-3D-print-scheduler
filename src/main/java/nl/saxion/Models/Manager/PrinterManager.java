package nl.saxion.Models.Manager;

import nl.saxion.Models.Printers.HousedPrinter;
import nl.saxion.Models.Printers.MultiColor;
import nl.saxion.Models.Printers.Printer;
import nl.saxion.Models.Printers.StandardFDM;
import nl.saxion.Models.Prints.FilamentType;
import nl.saxion.Models.Prints.Print;
import nl.saxion.Models.Prints.PrintTask;
import nl.saxion.Models.Prints.Spool;
import org.json.simple.JSONArray;
import java.util.*;

public class PrinterManager {
    //TODO: Changed all Arraylists to using List interface
    private List<Printer> printers = new ArrayList<>();
    private List<Print> prints = new ArrayList<>();
    private List<Spool> spools = new ArrayList<>();

    private List<Spool> freeSpools = new ArrayList<>(); // TODO: Decide if this should be used at all.
    private List<Printer> freePrinters = new ArrayList<>();
    private List<PrintTask> pendingPrintTasks = new ArrayList<>();
    private HashMap<Printer, PrintTask> runningPrintTasks = new HashMap();

    //TODO: Added a List for availableColors, this is a temporary solution until we know how to fix this problem.
    private List<String> availableColors = new ArrayList<>();

    //TODO: Added a Print Strategy. Changing through menu works, currently no Pattern implemented.
    private String printStrategy = "Less Spool Changes";

    //TODO: Add Pattern to adding a Printer? What if more printers are added later on?
    //TODO: Add ENUM to printerType?
    public void addPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, JSONArray currentSpools) {
        if (printerType == 1) {
            StandardFDM printer = new StandardFDM(id, printerName, manufacturer, maxX, maxY, maxZ);
            Spool cspool = getSpoolByID(((Long) currentSpools.get(0)).intValue());
            printer.setCurrentSpool(cspool);
            //TODO: Make Spools have 2 States? Used and unused. This removes the freeSpools list entirely.
            freeSpools.remove(cspool);
            printers.add(printer);
            //TODO: Make Printer have 2 States? Used and unused. This removes the freePrinters list entirely.
            freePrinters.add(printer);
        } else if (printerType == 2) {
            HousedPrinter printer = new HousedPrinter(id, printerName, manufacturer, maxX, maxY, maxZ);
            Spool cspool = getSpoolByID(((Long) currentSpools.get(0)).intValue());
            printer.setCurrentSpool(cspool);
            freeSpools.remove(cspool);
            printers.add(printer);
            freePrinters.add(printer);
        } else if (printerType == 3) {
            MultiColor printer = new MultiColor(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors);
            ArrayList<Spool> cspools = new ArrayList<>();
            cspools.add(getSpoolByID(((Long) currentSpools.get(0)).intValue()));
            cspools.add(getSpoolByID(((Long) currentSpools.get(1)).intValue()));
            cspools.add(getSpoolByID(((Long) currentSpools.get(2)).intValue()));
            cspools.add(getSpoolByID(((Long) currentSpools.get(3)).intValue()));
            printer.setCurrentSpools(cspools);
            for(Spool spool: cspools) {
                freeSpools.remove(spool);
            }
            printers.add(printer);
            freePrinters.add(printer);
        }
    }

    public boolean containsSpool(final List<Spool> list, final String name){
        return list.stream().anyMatch(o -> o.getColor().equals(name));
    }

    //TODO: How to break this entire method into smaller pieces?
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

                            //TODO: Changed this to a one liner
//                            for (Spool spool : printer.getCurrentSpools()) {
//                                freeSpools.add(spool);
//                            }
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

    public void startInitialQueue() {
        for(Printer printer: printers) {
            selectPrintTask(printer);
        }
    }

    //TODO: Added method that creates a new Print and adds it to a List
    public void addPrint(String name, String filename, int height, int width, int length, ArrayList<Integer> filamentLength) {
        Print p = new Print(name, filename, height, width, length, filamentLength);
        prints.add(p);
    }

    //TODO: Added method that creates a new Spool and adds it to a List
    public void addSpool(int id, String color, FilamentType filamentType, double length) {
        Spool s = new Spool(id, color, filamentType, length);
        spools.add(s);
    }

//    public List<Print> getPrints() {
//        return prints;
//    }

    public PrintTask getPrinterCurrentTask(Printer printer) {
        if(!runningPrintTasks.containsKey(printer)) {
            return null;
        }
        return runningPrintTasks.get(printer);
    }

    public List<PrintTask> getPendingPrintTasks() {
        return pendingPrintTasks;
    }

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

    public Print findPrint(String printName) {
        for (Print p : prints) {
            if (p.getName().equals(printName)) {
                return p;
            }
        }
        return null;
    }

    public Print findPrint(int index) {
        if(index > prints.size() -1) {
            return null;
        }
        return prints.get(index);
    }

    public void addSpool(Spool spool) {
        spools.add(spool);
        freeSpools.add(spool);
    }

    public Spool getSpoolByID(int id) {
        for(Spool s: spools) {
            if(s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    //TODO: Make this register on observation?
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
        selectPrintTask(printer);
    }

    //TODO: Make this register on observation?
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

    private void printError(String s) {
        System.out.println("Error: "+s);
        System.out.println("Press Enter to continue");
        new Scanner(System.in).nextLine();
    }

    public void addAvailableColor(String colorString) {
        availableColors.add(colorString);
    }

    public String getPrintStrategy() {
        return printStrategy;
    }

    public List<Print> getAvailablePrints() {
        return prints;
    }

    public List<String> getAvailableColors() {
        return availableColors;
    }

    public List<Spool> getSpools() {
        return spools;
    }
    public List<Printer> getPrinters() {
        return printers;
    }

    public void setPrintStrategy(String printStrategy) {
        this.printStrategy = printStrategy;
    }

}
