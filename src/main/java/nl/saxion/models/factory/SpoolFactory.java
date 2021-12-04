package nl.saxion.models.factory;

import nl.saxion.models.manager.PrinterManager;
import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.Spool;

public class SpoolFactory extends PrintSchedulerFactory {

    public SpoolFactory(PrinterManager printerManager) {
        super(printerManager);
    }

    public void createSpool(int id, String color, FilamentType filamentType, double length) {
        Spool s = new Spool(id, color, filamentType, length);
        printerManager.addSpool(s);
    }
}
