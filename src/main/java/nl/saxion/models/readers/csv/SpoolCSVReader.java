package nl.saxion.models.readers.csv;

import nl.saxion.models.factories.SpoolFactory;
import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.Spool;
import nl.saxion.models.readers.DataReader;
import nl.saxion.models.readers.ReaderException;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public class SpoolCSVReader extends DataReader<Spool> {

    SpoolFactory spoolFactory;

    public SpoolCSVReader(String source, SpoolFactory spoolFactory) {
        super(source);
        this.spoolFactory = spoolFactory;
    }

    @Override
    public void readItems() throws ReaderException {
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