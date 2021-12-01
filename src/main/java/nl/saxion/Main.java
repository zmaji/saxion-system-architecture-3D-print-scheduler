package nl.saxion;

import nl.saxion.Models.Facade.PrinterFacade;
import nl.saxion.Models.Prints.FilamentType;
import nl.saxion.Models.Prints.Print;
import nl.saxion.Models.Prints.PrintTask;
import nl.saxion.Models.Prints.Spool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

//TODO: Complete Main + menu werkt behalve het toevoegen van een nieuwe Print Task, omdat het niet lukt om een Print Object mee te geven (zie comments in methodes)
//TODO: Todo's zijn comments over wat er is veranderd (Om geel te laten opvallen) en soms vragen voor de toekomst
//TODO: Oude code is commented. In zeldzame gevallen is code commented die momenteel (nog) niet werkt. Bijvoorbeeld printerFacade.addPrintTask(printName, colors, chosenType);

public class Main {
    Scanner scanner = new Scanner(System.in);
    private PrinterFacade printerFacade = new PrinterFacade();
//    private PrinterManager manager = new PrinterManager();

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
                System.out.println("Temp String for testing");
                registerPrintCompletion();
            } else if (choice == 3) {
                System.out.println("Temp String for testing");
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
        printerFacade.startInitialQueue();
    }

    private void exit() {
    }

    // This method only changes the name but does not actually work.
    // It exists to demonstrate the output.
    // in the future strategy might be added.
    // TODO: Changed getting and setting the Strategy from Facade + PrinterManager
    private void changePrintStrategy() {
        System.out.println("Current strategy: " + printerFacade.getPrintStrategy());
        System.out.println("1: Less Spool Changes");
        System.out.println("2: Efficient Spool Usage");
        System.out.println("Choose strategy: ");
        int strategyChoice = numberInput(1, 2);
        if(strategyChoice == 1) {
            printerFacade.setPrintStrategy("Less Spool Changes");
        } else if( strategyChoice == 2) {
            printerFacade.setPrintStrategy("Efficient Spool Usage");
        }
    }

    // TODO: Move function to register print completion in menu completely to being automatically updated by Observer?
    private void registerPrintCompletion() {

        //TODO: Moved this to showCurrentRunningPrinters method
//        ArrayList<Printer> printers = manager.getPrinters();
//        System.out.println("---------- Currently Running Printers ----------");
//        for(Printer p: printers) {
//            PrintTask printerCurrentTask= manager.getPrinterCurrentTask(p);
//            if(printerCurrentTask != null) {
//                System.out.println(p.getId() + ": " +p.getName() + " - " + printerCurrentTask);
//            }
//        }
        showCurrentRunningPrinters();

        //TODO: How to do this part?
        System.out.print("Printer that is done (ID): ");
        int printerId = numberInput(1, printerFacade.getCurrentlyRunningPrinters().size());
        printerFacade.registerCompletion(printerId);
    }

      //TODO: Move function to register print failure in menu completely to being automatically updated by Observer?
    private void registerPrinterFailure() {

        //TODO: Moved this to showCurrentRunningPrinters method
//        ArrayList<Printer> printers = manager.getPrinters();
//        System.out.println("---------- Currently Running Printers ----------");
//        for(Printer p: printers) {
//            PrintTask printerCurrentTask= manager.getPrinterCurrentTask(p);
//            if(printerCurrentTask != null) {
//                System.out.println(p.getId() + ": " +p.getName() + " - " + printerCurrentTask);
//            }
//        }
        showCurrentRunningPrinters();

        //TODO: How to do this part?
        System.out.print("Printer ID that failed: ");
        int printerId = numberInput(1, printerFacade.getCurrentlyRunningPrinters().size());
        printerFacade.registerFailure(printerId);
    }

    //TODO: Move this to PrinterManager?
    private void addNewPrintTask() {
//        List<String> colors = new ArrayList<>();
//        var prints = manager.getPrints();
        //TODO: Changed this to using showPrints method
//        System.out.println("---------- Available prints ----------");
//        int counter = 1;
//        for (var p : prints) {
//            System.out.println(counter + ": " + p.getName());
//            counter++;
//        }
//        System.out.println("--------------------------------------");
        showPrints();

        System.out.print("Print number: ");
        //TODO: Changed this to using created findPrintOnInput method. Can you read input in this method?
//        int printNumber = numberInput(1, prints.size());
//        Print print = manager.findPrint(printNumber - 1);
        String printName = findPrintOnInput();

        //TODO: Changed this to using created showAvailableFilamentTypes method. Can you read input in this method?
//        System.out.println("---------- Filament Type ----------");
//        System.out.println("1: PLA");
//        System.out.println("2: PETG");
//        System.out.println("3: ABS");
        showAvailableFilamentTypes();

        System.out.print("Filament type number: ");
        FilamentType chosenType = findFilamentTypeOnInput();

        //TODO: Changed this to using showAvailableSpools method.
//        var spools = manager.getSpools();
//        System.out.println("---------- Spools ----------");

//        counter = 1;
//        for (var spool : spools) {
//            String colorString = spool.getColor();
//            if(type == spool.getFilamentType() && !availableColors.contains(colorString)) {
//                System.out.println(counter + ": " + colorString + " (" + spool.getFilamentType() + ")");
//                availableColors.add(colorString);
//                counter++;
//            }
//        }
//        System.out.println("----------------------------");
        showAvailableSpools(chosenType);

        System.out.print("Color number: ");
        //TODO: Changed this to created findColorOnInput method. Can you read input in this method?
//        int colorChoice = numberInput(1, availableColors.size());
//        colors.add(availableColors.get(colorChoice-1));
//        for(int i = 1; i < print.getFilamentLength().size(); i++) {
//            System.out.print("Color number: ");
//            colorChoice = numberInput(1, availableColors.size());
//            colors.add(availableColors.get(colorChoice-1));
//        }
        //TODO: How to give Print object to this method?
//        List<String> colors = findColorsOnInput(printName);

        //TODO: Method needs String, currently giving an int, how to change???
//        printerFacade.addPrintTask(printName, colors, chosenType);
    }

    //TODO: Changed this to method below
//    private void showPrints() {
//        var prints = manager.getPrints();
//        System.out.println("---------- Available prints ----------");
//        for (var p : prints) {
//            System.out.println(p);
//        }
//        System.out.println("--------------------------------------");
//    }

    //TODO: Idee is om hier bij elke methode de printlines mee te geven en het Printen aan de Facade over te houden, die vervolgens weer een lijst krijg van Manager.
    //TODO: Is dit een slim idee? Of verplaatsen we dit naar Facade?

    //TODO: Change entire method to printerFacade.showAvailablePrints?
    private void showPrints() {
        System.out.println("---------- Available prints ----------");
        printerFacade.printAvailablePrints();
        System.out.println("--------------------------------------");
    }

    //TODO: Made a method that prints available Spools
    private void showAvailableSpools(FilamentType type) {
        System.out.println("---------- Spools ----------");
        printerFacade.printAvailableSpools(type);
        System.out.println("--------------------------------------");
    }

    //TODO: Made a method that prints available Filament Types
    private void showAvailableFilamentTypes() {
        System.out.println("---------- Filament Type ----------");
        printerFacade.printAvailableFilamentTypes();
        System.out.println("--------------------------------------");
    }

    //TODO: Made a method that prints running Printers
    private void showCurrentRunningPrinters() {
        System.out.println("---------- Currently Running Printers ----------");
        printerFacade.printCurrentRunningPrinters();
        System.out.println("--------------------------------------");
    }

    //TODO: Made a method that searches for a print on user input.
    private String findPrintOnInput() {
        List<Print> prints = printerFacade.getAvailablePrints();
        int printNumber = numberInput(1, prints.size());
        Print foundPrint = printerFacade.findPrint(printNumber - 1);
        return foundPrint.getName();
    }

    //TODO: Made a method that searches for a filament type on user input.
    private FilamentType findFilamentTypeOnInput() {
        int filamentType = numberInput(1, 3);
        FilamentType type = null;
        switch (filamentType) {
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
        }
        return type;
    }

    //TODO: Made a method that adds colors to a List based on user input.
    //TODO: How to give Print object to this method?
    private List<String> findColorsOnInput(Print print) {
        List<String> colors = printerFacade.getAvailableColors();
        int colorChoice = numberInput(1, colors.size());
        colors.add(colors.get(colorChoice-1));
        for(int i = 1; i < print.getFilamentLength().size(); i++) {
            System.out.print("Color number: ");
            colorChoice = numberInput(1, colors.size());
            colors.add(colors.get(colorChoice-1));
        }
        return colors;
    }

    //TODO: Moved certain responsibilities to Facade + Manager
    private void showSpools() {
//        var spools = manager.getSpools();
        System.out.println("---------- Spools ----------");
//        for (var spool : spools) {
//            System.out.println(spool);
//        }
        printerFacade.printAllSpools();
        System.out.println("----------------------------");
    }

    //TODO: Moved certain responsibilities to Facade + Manager
    private void showPrinters() {
//        var printers = manager.getPrinters();
        System.out.println("--------- Available printers ---------");
//        for (var p : printers) {
//            System.out.print(p);
//            PrintTask currentTask = manager.getPrinterCurrentTask(p);
//            if(currentTask != null) {
//                System.out.println("Current Print Task: " + currentTask);
//            }
//            System.out.println();
//        }
        printerFacade.printAllPrinters();
        System.out.println("--------------------------------------");
    }

    //TODO: Moved certain responsibilities to Facade + Manager
    private void showPendingPrintTasks() {
//        ArrayList<PrintTask> printTasks = manager.getPendingPrintTasks();
        System.out.println("--------- Pending Print Tasks ---------");
//        for (var p : printTasks) {
//            System.out.println(p);
//        }
        printerFacade.printAllPendingPrintTasks();
        System.out.println("--------------------------------------");
    }

    //TODO: Move to JSONReader Class
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
//                for(int i = 0; i < fLength.size(); i++) {
//                    filamentLength.add(((Long)fLength.get(i)).intValue());
//                }
                //TODO: Auto-changed for-loop
                for (Object o : fLength) {
                    filamentLength.add(((Long) o).intValue());
                }

                //TODO: Moved this to Facade + Manager.
                printerFacade.addPrint(name, filename, height, width, length, filamentLength);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //TODO: Move to JSONReader Class
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

                //TODO: Moved this to Facade + Manager.
                printerFacade.addPrinter(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //TODO: Move to JSONReader Class
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

                //TODO: Moved this to Facade + Manager.
                printerFacade.addSpool(id, color, type, length);
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
