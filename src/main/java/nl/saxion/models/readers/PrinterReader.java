package nl.saxion.models.readers;

import nl.saxion.models.factories.PrinterFactory;
import nl.saxion.models.printers.Printer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

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

                JSONArray currentSpools = (JSONArray) printer.get("currentSpools");

                printerFactory.createPrinter(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
