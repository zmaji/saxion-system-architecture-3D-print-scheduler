package nl.saxion;

import nl.saxion.Models.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    Scanner scanner = new Scanner(System.in);
    private PrinterManager manager = new PrinterManager();

    private String printStrategy = "Less Spool Changes";

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        readPrintsFromFile();
        readSpoolsFromFile();
        readPrintersFromFile();
        int choice = 1;
        while (choice > 0 && choice < 10) {
            menu();
            choice = menuChoice(9);
            if (choice == 1) {
                addNewPrintTask();
            } else if (choice == 2) {
                registerPrintCompletion();
            } else if (choice == 3) {
                registerPrinterFailure();
            } else if (choice == 4) {
                changePrintStrategy();
            } else if (choice == 5) {
                startPrintQueue();
            } else if (choice == 6) {
                showPrints();
            } else if (choice == 7) {
                showPrinters();
            } else if (choice == 8) {
                showSpools();
            } else if (choice == 9) {
                showPendingPrintTasks();
            }
        }
        exit();
    }

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

    private void startPrintQueue() {
        manager.startInitialQueue();
    }

    private void exit() {

    }

    // This method only changes the name but does not actually work.
    // It exists to demonstrate the output.
    // in the future strategy might be added.
    private void changePrintStrategy() {
        System.out.println("Current strategy: " + printStrategy);
        System.out.println("1: Less Spool Changes");
        System.out.println("2: Efficient Spool Usage");
        System.out.println("Choose strategy: ");
        int strategyChoice = numberInput(1, 2);
        if(strategyChoice == 1) {
            printStrategy = "Less Spool Changes";
        } else if( strategyChoice == 2) {
            printStrategy = "Efficient Spool Usage";
        }
    }

    // TODO: This should be based on which printer is finished printing.
    private void registerPrintCompletion() {
        ArrayList<Printer> printers = manager.getPrinters();
        System.out.println("---------- Currently Running Printers ----------");
        for(Printer p: printers) {
            PrintTask printerCurrentTask= manager.getPrinterCurrentTask(p);
            if(printerCurrentTask != null) {
                System.out.println(p.getId() + ": " +p.getName() + " - " + printerCurrentTask);
            }
        }
        System.out.print("Printer that is done (ID): ");
        int printerId = numberInput(1, printers.size());
        manager.registerCompletion(printerId);
    }

    private void registerPrinterFailure() {
        ArrayList<Printer> printers = manager.getPrinters();
        System.out.println("---------- Currently Running Printers ----------");
        for(Printer p: printers) {
            PrintTask printerCurrentTask= manager.getPrinterCurrentTask(p);
            if(printerCurrentTask != null) {
                System.out.println(p.getId() + ": " +p.getName() + " - " + printerCurrentTask);
            }
        }
        System.out.print("Printer ID that failed: ");
        int printerId = numberInput(1, printers.size());
        manager.registerPrinterFailure(printerId);
    }

    private void addNewPrintTask() {
        List<String> colors = new ArrayList<>();
        var prints = manager.getPrints();
        System.out.println("---------- Available prints ----------");
        int counter = 1;
        for (var p : prints) {
            System.out.println(counter + ": " + p.getName());
            counter++;
        }
        System.out.println("--------------------------------------");
        System.out.print("Print number: ");
        int printNumber = numberInput(1, prints.size());
        Print print = manager.findPrint(printNumber - 1);
        String printName = print.getName();
        System.out.println("---------- Filament Type ----------");
        System.out.println("1: PLA");
        System.out.println("2: PETG");
        System.out.println("3: ABS");
        System.out.print("Filament type number: ");
        int ftype = numberInput(1, 3);
        FilamentType type;
        switch (ftype) {
            case 1:
                type = FilamentType.PLA;
                break;
            case 2:
                type = FilamentType.PETG;
                break;
            case 3:
                type = FilamentType.ABS;
                break;
            default:
                System.out.println("Not a valid filamentType, bailing out");
                return;
        }
        var spools = manager.getSpools();
        System.out.println("---------- Spools ----------");
        ArrayList<String> availableColors = new ArrayList<>();
        counter = 1;
        for (var spool : spools) {
            String colorString = spool.getColor();
            if(type == spool.getFilamentType() && !availableColors.contains(colorString)) {
                System.out.println(counter + ": " + colorString + " (" + spool.getFilamentType() + ")");
                availableColors.add(colorString);
                counter++;
            }
        }
        System.out.println("----------------------------");
        System.out.print("Color number: ");
        int colorChoice = numberInput(1, availableColors.size());
        colors.add(availableColors.get(colorChoice-1));
        for(int i = 1; i < print.getFilamentLength().size(); i++) {
            System.out.print("Color number: ");
            colorChoice = numberInput(1, availableColors.size());
            colors.add(availableColors.get(colorChoice-1));
        }
        manager.addPrintTask(printName, colors, type);
    }

    private void showPrints() {
        var prints = manager.getPrints();
        System.out.println("---------- Available prints ----------");
        for (var p : prints) {
            System.out.println(p);
        }
        System.out.println("--------------------------------------");
    }

    private void showSpools() {
        var spools = manager.getSpools();
        System.out.println("---------- Spools ----------");
        for (var spool : spools) {
            System.out.println(spool);
        }
        System.out.println("----------------------------");
    }

    private void showPrinters() {
        var printers = manager.getPrinters();
        System.out.println("--------- Available printers ---------");
        for (var p : printers) {
            System.out.print(p);
            PrintTask currentTask = manager.getPrinterCurrentTask(p);
            if(currentTask != null) {
                System.out.println("Current Print Task: " + currentTask);
            }
            System.out.println();
        }
        System.out.println("--------------------------------------");
    }

    private void showPendingPrintTasks() {
        ArrayList<PrintTask> printTasks = manager.getPendingPrintTasks();
        System.out.println("--------- Pending Print Tasks ---------");
        for (var p : printTasks) {
            System.out.println(p);
        }
        System.out.println("--------------------------------------");
    }

    private void readPrintsFromFile() {
        JSONParser jsonParser = new JSONParser();
        URL printResource = getClass().getResource("/prints.json");
        if (printResource == null) {
            System.err.println("Warning: Could not find prints.json file");
            return;
        }
        try (FileReader reader = new FileReader(printResource.getFile())) {
            JSONArray prints = (JSONArray) jsonParser.parse(reader);
            for (Object p : prints) {
                JSONObject print = (JSONObject) p;
                String name = (String) print.get("name");
                String filename = (String) print.get("filename");
                int height = ((Long) print.get("height")).intValue();
                int width = ((Long) print.get("width")).intValue();
                int length = ((Long) print.get("length")).intValue();
                //int filamentLength = ((Long) print.get("filamentLength")).intValue();
                JSONArray fLength = (JSONArray) print.get("filamentLength");
                ArrayList<Integer> filamentLength = new ArrayList();
                for(int i = 0; i < fLength.size(); i++) {
                    filamentLength.add(((Long)fLength.get(i)).intValue());
                }
                manager.addPrint(name, filename, height, width, length, filamentLength);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void readPrintersFromFile() {
        JSONParser jsonParser = new JSONParser();
        URL printersResource = getClass().getResource("/printers.json");
        if (printersResource == null) {
            System.err.println("Warning: Could not find printers.json file");
            return;
        }
        try (FileReader reader = new FileReader(printersResource.getFile())) {
            JSONArray printers = (JSONArray) jsonParser.parse(reader);
            for (Object p : printers) {
                JSONObject printer = (JSONObject) p;
                int id = ((Long) printer.get("id")).intValue();
                int type = ((Long) printer.get("type")).intValue();
                String name = (String) printer.get("name");
                String manufacturer = (String) printer.get("manufacturer");
                int maxX = ((Long) printer.get("maxX")).intValue();
                int maxY = ((Long) printer.get("maxY")).intValue();
                int maxZ = ((Long) printer.get("maxZ")).intValue();
                int maxColors = ((Long) printer.get("maxColors")).intValue();
                // TODO: Add current Spool
                JSONArray currentSpools = (JSONArray) printer.get("currentSpools");
                manager.addPrinter(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void readSpoolsFromFile() {
        JSONParser jsonParser = new JSONParser();
        URL spoolsResource = getClass().getResource("/spools.json");
        if (spoolsResource == null) {
            System.err.println("Warning: Could not find spools.json file");
            return;
        }
        try (FileReader reader = new FileReader(spoolsResource.getFile())) {
            JSONArray spools = (JSONArray) jsonParser.parse(reader);
            for (Object p : spools) {
                JSONObject spool = (JSONObject) p;
                int id = ((Long) spool.get("id")).intValue();
                String color = (String) spool.get("color");
                String filamentType = (String) spool.get("filamentType");
                double length = (Double) spool.get("length");
                FilamentType type;
                switch (filamentType) {
                    case "PLA":
                        type = FilamentType.PLA;
                        break;
                    case "PETG":
                        type = FilamentType.PETG;
                        break;
                    case "ABS":
                        type = FilamentType.ABS;
                        break;
                    default:
                        System.out.println("Not a valid filamentType, bailing out");
                        return;
                }
                manager.addSpool(new Spool(id, color, type, length)); // Geen nieuwe spool maken in main maar alleen benodigde velden meegeven aan manager om daar vervolgens de spool aan te maken
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public int menuChoice(int max) {
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

    public String stringInput() {
        String input = null;
        while(input == null || input.length() == 0){
            input = scanner.nextLine();
        }
        return input;
    }

    public int numberInput() {
        int input = scanner.nextInt();
        return input;
    }

    public int numberInput(int min, int max) {
        int input = numberInput();
        while (input < min || input > max) {
            input = numberInput();
        }
        return input;
    }
}
