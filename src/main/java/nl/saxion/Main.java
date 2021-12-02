package nl.saxion;

import nl.saxion.Facade.PrinterFacade;
import nl.saxion.Models.Prints.FilamentType;
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
                printerFacade.registerPrintCompletion();
            } else if (choice == 3) {
                printerFacade.registerPrinterFailure();
            } else if (choice == 4) {
                printerFacade.changePrintStrategy();
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
                    case "PLA" -> type = FilamentType.PLA;
                    case "PETG" -> type = FilamentType.PETG;
                    case "ABS" -> type = FilamentType.ABS;
                    default -> {
                        System.out.println("Not a valid filamentType, bailing out");
                        return;
                    }
                }

                //TODO: Moved this to Facade + Manager.
                printerFacade.addSpool(id, color, type, length);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
