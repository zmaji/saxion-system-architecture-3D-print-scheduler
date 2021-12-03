package nl.saxion.models.printers;

import nl.saxion.models.prints.Print;
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
        return new Spool[0];
    }

    @Override
    public Spool getCurrentSpool() {
        return null;
    }

    @Override
    public void setCurrentSpools(ArrayList<Spool> spools) {

    }

    @Override
    public void setCurrentSpool(Spool spool) {

    }

    @Override
    public boolean printFits(Print print) {
        return false;
    }
}
