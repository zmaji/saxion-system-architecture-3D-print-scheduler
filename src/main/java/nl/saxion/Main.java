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

public class Main {
    private final PrinterFacade printerFacade = new PrinterFacade();

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
            choice = Helper.menuChoice(9);
            if (choice == 1) {
                printerFacade.addNewPrintTask();
            } else if (choice == 2) {
                System.out.println("Temp String for testing");
                registerPrintCompletion();
            } else if (choice == 3) {
                System.out.println("Temp String for testing");
                registerPrinterFailure();
            } else if (choice == 4) {
                changePrintStrategy();
            } else if (choice == 5) {
                printerFacade.startInitialQueue();
            } else if (choice == 6) {
                printerFacade.showItems("Available prints", "prints");
            } else if (choice == 7) {
                printerFacade.showItems("Available printers", "printers");
            } else if (choice == 8) {
                printerFacade.showItems("Spools", "spools");
            } else if (choice == 9) {
                printerFacade.showItems("Pending Print Tasks", "pendingTasks");
            }
        }
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

    // This method only changes the name but does not actually work.
    // It exists to demonstrate the output.
    // in the future strategy might be added.
    // TODO: Changed getting and setting the Strategy from Facade + PrinterManager
    private void changePrintStrategy() {
        System.out.println("Current strategy: " + printerFacade.getPrintStrategy());
        System.out.println("1: Less Spool Changes");
        System.out.println("2: Efficient Spool Usage");
        System.out.println("Choose strategy: ");
        int strategyChoice = Helper.numberInput(1, 2);
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
        int printerId = Helper.numberInput(1, printerFacade.getCurrentlyRunningPrinters().size());
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
        int printerId = Helper.numberInput(1, printerFacade.getCurrentlyRunningPrinters().size());
        printerFacade.registerFailure(printerId);
    }

    //TODO: Move this to PrinterManager?
//    private void addNewPrintTask() {
////        List<String> colors = new ArrayList<>();
////        var prints = manager.getPrints();
//        //TODO: Changed this to using showPrints method
////        System.out.println("---------- Available prints ----------");
////        int counter = 1;
////        for (var p : prints) {
////            System.out.println(counter + ": " + p.getName());
////            counter++;
////        }
////        System.out.println("--------------------------------------");
//        showPrints();
//
//        System.out.print("Print number: ");
//        //TODO: Changed this to using created findPrintOnInput method. Can you read input in this method?
////        int printNumber = numberInput(1, prints.size());
////        Print print = manager.findPrint(printNumber - 1);
//        String printName = findPrintOnInput();
//
//        //TODO: Changed this to using created showAvailableFilamentTypes method. Can you read input in this method?
////        System.out.println("---------- Filament Type ----------");
////        System.out.println("1: PLA");
////        System.out.println("2: PETG");
////        System.out.println("3: ABS");
//        showAvailableFilamentTypes();
//
//        System.out.print("Filament type number: ");
//        FilamentType chosenType = findFilamentTypeOnInput();
//
//        //TODO: Changed this to using showAvailableSpools method.
////        var spools = manager.getSpools();
////        System.out.println("---------- Spools ----------");
//
////        counter = 1;
////        for (var spool : spools) {
////            String colorString = spool.getColor();
////            if(type == spool.getFilamentType() && !availableColors.contains(colorString)) {
////                System.out.println(counter + ": " + colorString + " (" + spool.getFilamentType() + ")");
////                availableColors.add(colorString);
////                counter++;
////            }
////        }
////        System.out.println("----------------------------");
//        showAvailableSpools(chosenType);
//
//        System.out.print("Color number: ");
//        //TODO: Changed this to created findColorOnInput method. Can you read input in this method?
////        int colorChoice = numberInput(1, availableColors.size());
////        colors.add(availableColors.get(colorChoice-1));
////        for(int i = 1; i < print.getFilamentLength().size(); i++) {
////            System.out.print("Color number: ");
////            colorChoice = numberInput(1, availableColors.size());
////            colors.add(availableColors.get(colorChoice-1));
////        }
//        //TODO: How to give Print object to this method?
////        List<String> colors = findColorsOnInput(printName);
//
//        //TODO: Method needs String, currently giving an int, how to change???
////        printerFacade.addPrintTask(printName, colors, chosenType);
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


    //TODO: Made a method that searches for a filament type on user input.


    //TODO: Made a method that adds colors to a List based on user input.
    //TODO: How to give Print object to this method?


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
}
