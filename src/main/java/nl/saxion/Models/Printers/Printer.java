package nl.saxion.models.printers;

import nl.saxion.models.prints.Print;
import nl.saxion.models.prints.Spool;

import java.util.ArrayList;

public abstract class Printer {
    protected int id;
    protected String name;
    protected String manufacturer;
    protected final int maxX;
    protected final int maxY;
    protected final int maxZ;
    protected Spool[] spools;
    protected Spool currentSpool;

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
