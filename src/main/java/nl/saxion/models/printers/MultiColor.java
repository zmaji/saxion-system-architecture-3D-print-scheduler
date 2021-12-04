package nl.saxion.models.printers;

import nl.saxion.models.prints.Print;
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
        this.spools = new Spool[spools.size()];
        for (int i = 0; i < spools.size(); i++) {
            this.spools[i] = spools.get(i);
        }
        setCurrentSpool(spools.get(0));
    }

    @Override
    public void setCurrentSpool(Spool spool) {
        this.currentSpool = spool;
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
        Spool[] spools = this.spools;
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
        for (int i = 0; i < spools.length; i++) {
            if (spools[i] != currentSpool) {
                result += "spool "+ (i+1) +": " + spools[i].getId() + System.lineSeparator();
            }
        }
        return result;
    }

    public int getMaxColors() {
        return maxColors;
    }
}
