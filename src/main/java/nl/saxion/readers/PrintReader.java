package nl.saxion.readers;

import nl.saxion.Models.Prints.Print;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PrintReader extends JSONReader<Print> {
    public PrintReader(String source) {
        super(source);
    }

    @Override
    public List<Print> readPrintsFromFile() throws ReaderException {
        JSONParser jsonParser = new JSONParser();
        URL printResource = getClass().getResource(source);
        if (printResource == null) {
            throw new ReaderException("Warning: Could not find prints.json file");
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
                ArrayList<Integer> filamentLength = new ArrayList<>();
                for (Object o : fLength) {
                    filamentLength.add(((Long) o).intValue());
                }
                this.readItems.add(new Print(name, filename, height, width, length, filamentLength));
            }

            return readItems;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
