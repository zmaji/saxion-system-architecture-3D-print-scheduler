package nl.saxion.readers;

import nl.saxion.models.printers.Printer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PrinterReader extends JSONReader<Printer> {
    public PrinterReader(String source) {
        super(source);
    }

    @Override
    public List<Printer> readPrintsFromFile() throws ReaderException {
        JSONParser jsonParser = new JSONParser();
        URL printResource = getClass().getResource(source);
        if (printResource == null) {
            throw new ReaderException("Warning: Could not find prints.json file");
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
//                this.readItems.add(new Printe(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools));
            }

            return readItems;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
