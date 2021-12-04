package nl.saxion.models.readers;

import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.Print;
import nl.saxion.models.prints.Spool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SpoolReader extends JSONReader<Spool> {
    public SpoolReader(String source) {
        super(source);
    }

    @Override
    public List<Spool> readPrintsFromFile() throws ReaderException {
        JSONParser jsonParser = new JSONParser();
        URL printResource = getClass().getResource(source);
        if (printResource == null) {
            throw new ReaderException("Warning: Could not find "+ source +" file");
        }
        try (FileReader reader = new FileReader(printResource.getFile())) {
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
                        System.err.println("Not a valid filamentType, bailing out");
                        return null;
                    }
                }
                this.readItems.add(new Spool(id, color, type, length));
            }

            return readItems;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
