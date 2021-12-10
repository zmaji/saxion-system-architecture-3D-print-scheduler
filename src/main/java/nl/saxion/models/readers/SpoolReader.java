package nl.saxion.models.readers;

import nl.saxion.models.factories.SpoolFactory;
import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.Spool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class SpoolReader extends JSONReader<Spool> {

    SpoolFactory spoolFactory;

    public SpoolReader(String source, SpoolFactory spoolFactory) {
        super(source);
        this.spoolFactory = spoolFactory;
    }

    @Override
    public void readItems() throws ReaderException {
        JSONParser jsonParser = new JSONParser();
        URL printResource = getClass().getResource(source);
        if (printResource == null) {
            throw new ReaderException("Warning: Could not find " + source + " file");
        }
        try (FileReader reader = new FileReader(printResource.getFile())) {
            JSONArray spools = (JSONArray) jsonParser.parse(reader);
            for (Object p : spools) {
                JSONObject spool = (JSONObject) p;
                int id = ((Long) spool.get("id")).intValue();
                String color = (String) spool.get("color");
                String filamentType = (String) spool.get("filamentType");
                double length = (Double) spool.get("length");
                FilamentType type = null;
                switch (filamentType) {
                    case "PLA" -> type = FilamentType.PLA;
                    case "PETG" -> type = FilamentType.PETG;
                    case "ABS" -> type = FilamentType.ABS;
                    default -> {
                        System.err.println("Not a valid filamentType, bailing out");
                    }
                }
                spoolFactory.createSpool(id, color, type, length);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void readItemsFromCSV() throws ReaderException {
        URL printResource = getClass().getResource(source);
        if (printResource == null) {
            throw new ReaderException("Warning: Could not find " + source + " file");
        }
        try (Scanner fileScanner = new Scanner(new File(printResource.getFile()))) {
            fileScanner.nextLine();
            while (fileScanner.hasNext()) {
                String[] fields = fileScanner.nextLine().split(",");
                int id = Integer.parseInt(fields[0]);
                String color = fields[1];
                FilamentType type = null;
                switch (fields[2]) {
                    case "PLA" -> type = FilamentType.PLA;
                    case "PETG" -> type = FilamentType.PETG;
                    case "ABS" -> type = FilamentType.ABS;
                    default -> {
                        System.err.println("Not a valid filamentType, bailing out");
                    }
                }
                double length = Double.parseDouble(fields[3]);
                spoolFactory.createSpool(id, color, type, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
