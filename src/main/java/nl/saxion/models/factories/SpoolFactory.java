package nl.saxion.models.factories;

import nl.saxion.models.manager.PrinterManager;
import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.Spool;

public class SpoolFactory extends PrintSchedulerFactory {

    public SpoolFactory(PrinterManager printerManager) {
        super(printerManager);
    }

    /** Creates a Spool based on given parameters and adds it to a List managed by PrinterManager
     *
     * @param id the id of the Spool
     * @param color the color of the Spool
     * @param filamentType the filamentType of the Spool
     * @param length the length of the Spool
     */
    public void createSpool(int id, String color, FilamentType filamentType, double length) {
        Spool s = new Spool(id, color, filamentType, length);
        printerManager.addSpool(s);
    }
}
