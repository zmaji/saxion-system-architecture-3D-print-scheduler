package nl.saxion.Models.Printers;

import nl.saxion.Models.Prints.Print;
import nl.saxion.Models.Prints.Spool;

import java.util.ArrayList;

public abstract class Printer {
    private int id;
    private String name;
    private String manufacturer;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private Spool[] spools;
    private Spool currentSpool;

    public Printer(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ) {
        this.id = id;
        this.name = printerName;
        this.manufacturer = manufacturer;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public int getId() {
        return id;
    }

    public abstract int CalculatePrintTime(String filename);

    public abstract Spool[] getCurrentSpools();

    public abstract Spool getCurrentSpool();

    public abstract void setCurrentSpools(ArrayList<Spool> spools);

    public abstract void setCurrentSpool(Spool spool);

    public abstract boolean printFits(Print print);

    @Override
    public String toString() {
        return  "ID: " + id + System.lineSeparator() +
                "Name: " + name + System.lineSeparator() +
                "Manufacturer: " + manufacturer + System.lineSeparator();
    }

    public String getName(){
        return name;
    }
}
