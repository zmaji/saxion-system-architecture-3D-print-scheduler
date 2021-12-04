package nl.saxion.models.readers;

import nl.saxion.models.factory.PrinterFactory;
import nl.saxion.models.printers.HousedPrinter;
import nl.saxion.models.printers.MultiColor;
import nl.saxion.models.printers.Printer;
import nl.saxion.models.printers.StandardFDM;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PrinterReader extends JSONReader<Printer> {

    PrinterFactory printerFactory;

    public PrinterReader(String source, PrinterFactory printerFactory) {
        super(source);
        this.printerFactory = printerFactory;
    }

    @Override
    public void readPrintsFromFile() throws ReaderException {
        JSONParser jsonParser = new JSONParser();
        URL printResource = getClass().getResource(source);
        if (printResource == null) {
            throw new ReaderException("Warning: Could not find " + source + " file");
        }
        try (FileReader reader = new FileReader(printResource.getFile())) {
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

                printerFactory.createPrinter(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools);
//                Printer readPrinter;
//                switch(type) {
//                    case 1 -> readPrinter = new StandardFDM(id, name, manufacturer, maxX, maxY, maxZ);
//                    case 2 -> readPrinter = new HousedPrinter(id, name, manufacturer, maxX, maxY, maxZ);
//                    case 3 -> readPrinter = new MultiColor(id, name, manufacturer, maxX, maxY, maxZ, maxColors);
//                    default -> throw new IllegalStateException("Unexpected value: " + type);
//                }

//                readPrinter.setCurrentSpool(currentSpools);
//                this.readItems.add(readPrinter);
//                this.readItems.add(new Printer(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools));
            }
//
//            return readItems;
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//
//        return null;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
