package nl.saxion.Models.Printers;

import nl.saxion.Models.Prints.Print;
import nl.saxion.Models.Prints.Spool;

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
        setCurrentSpool(spools.get(0));
        if(spools.size() > 1) spool2 = spools.get(1);
        if(spools.size() > 2) spool3 = spools.get(2);
        if(spools.size() > 3) spool4 = spools.get(3);
    }

    @Override
    public void setCurrentSpool(Spool spool) {

    }

    @Override
    public boolean printFits(Print print) {
        return false;
    }

    @Override
    public int CalculatePrintTime(String filename) {
        return 0;
    }

    @Override
    public Spool[] getCurrentSpools() {
        Spool[] spools = new Spool[4];
        spools[0] = getCurrentSpool();
        spools[1] = spool2;
        spools[2] = spool3;
        spools[3] = spool4;
        return spools;
    }

    @Override
    public Spool getCurrentSpool() {
        return null;
    }

    @Override
    public String toString() {
        String result = super.toString() +
                "maxColors: " + maxColors + System.lineSeparator();
        if(spool2 != null) {
            result += "spool2: " + spool2.getId() + System.lineSeparator();
        }
        if(spool3 != null) {
            result += "spool3: " + spool3.getId() + System.lineSeparator();
        }
        if(spool4 != null) {
            result += "spool4: " + spool4.getId() + System.lineSeparator();
        }
        return result;
    }

    public int getMaxColors() {
        return maxColors;
    }
}
