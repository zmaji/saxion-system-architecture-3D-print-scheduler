package nl.saxion.Models.printers;

import nl.saxion.Models.prints.Print;
import nl.saxion.Models.prints.Spool;

import java.util.ArrayList;

/* Standard cartesian FDM printer */
public class StandardFDM extends Printer {


    public StandardFDM(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ);
    }

    @Override
    public void setCurrentSpools(ArrayList<Spool> spools) {
        this.currentSpool = spools.get(0);
    }

    @Override
    public void setCurrentSpool(Spool spool) {
        this.currentSpool = spool;
    }

    @Override
    public Spool getCurrentSpool() {
        return currentSpool;
    }

    @Override
    public Spool[] getCurrentSpools() {
        Spool[] spools = new Spool[1];
        spools[0] = currentSpool;
        return spools;
    }

    @Override
    public boolean printFits(Print print) {
        return print.getHeight() <= maxZ && print.getWidth() <= maxX && print.getLength() <= maxY;
    }

    @Override
    public int CalculatePrintTime(String filename) {
        return 0;
    }

    @Override
    public String toString() {
        String result = super.toString() +
                "maxX: " + maxX + System.lineSeparator() +
                "maxY: " + maxY + System.lineSeparator() +
                "maxZ: " + maxZ + System.lineSeparator();
        if (currentSpool != null) {
            result += "Current spool: " + currentSpool.getId()+ System.lineSeparator();
        }
        return result;
    }
}
