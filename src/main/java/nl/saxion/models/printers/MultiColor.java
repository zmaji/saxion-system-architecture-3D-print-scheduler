package nl.saxion.models.printers;

import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.Print;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;

import java.util.ArrayList;

/* Printer capable of printing multiple colors. */
public class MultiColor extends Printer {
    private int maxColors;

    public MultiColor(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ);
        this.maxColors = maxColors;
    }

    @Override
    public void setCurrentSpools(ArrayList<Spool> spools) {
        this.spools = new Spool[4];
        setCurrentSpool(spools.get(0));
        if(spools.size() > 1) this.spools[1] = spools.get(1);
        if(spools.size() > 2) this.spools[2] = spools.get(2);
        if(spools.size() > 3) this.spools[3] = spools.get(3);
    }

    @Override
    public void setCurrentSpool(Spool spool) {
        this.currentSpool = spool;
    }

    @Override
    public boolean printFits(Print print) {
        return print.getHeight() <= maxZ && print.getWidth() <= maxX && print.getLength() <= maxY;
    }

    @Override
    public boolean printerCompatibleWithTask(PrintTask printTask) {
        return printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() <= this.getMaxColors();
    }

    @Override
    public int CalculatePrintTime(String filename) {
        return 0;
    }

    @Override
    public Spool[] getCurrentSpools() {
        Spool[] spools = new Spool[4];
        spools[0] = this.currentSpool;
        spools[1] = this.spools[1];
        spools[2] = this.spools[2];
        spools[3] = this.spools[3];
        return spools;
    }

    @Override
    public Spool getCurrentSpool() {
        return this.currentSpool;
    }

    @Override
    public String toString() {
        String result = super.toString() +
                "maxColors: " + maxColors + System.lineSeparator();
        for (int i = 0; i < spools.length; i++) {
            if (spools[i] != currentSpool) {
                result += "spool"+ (i+1) +": " + spools[i].getId() + System.lineSeparator();
            }
        }
        return result;
    }

    public int getMaxColors() {
        return maxColors;
    }
}
