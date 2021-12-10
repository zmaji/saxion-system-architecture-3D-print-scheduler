package nl.saxion.models.printers;

import nl.saxion.models.prints.Print;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;

import java.util.ArrayList;

/* Printer capable of printing ABS */
public class HousedPrinter extends Printer {

    public HousedPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ);
    }

    @Override
    public int CalculatePrintTime(String filename) {
        return 0;
    }

    @Override
    public Spool[] getCurrentSpools() {
        Spool[] spools = new Spool[1];
        spools[0] = currentSpool;
        return spools;
    }

    @Override
    public Spool getCurrentSpool() {
        return currentSpool;
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
        return true;
    }

    @Override
    public boolean printerCompatibleWithTask(PrintTask printTask) {
        return printTask.getColors().size() == 1;
    }

    @Override
    public String toString() {
        String result = super.toString();
        if (currentSpool != null) {
            result += "Current spool: " + currentSpool.getId()+ System.lineSeparator();
        }
        return result;
    }
}
