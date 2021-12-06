package nl.saxion.models.factories;

import nl.saxion.models.manager.PrinterManager;
import nl.saxion.models.prints.Print;

import java.util.ArrayList;

public class PrintFactory extends PrintSchedulerFactory {

    public PrintFactory(PrinterManager printerManager) {
        super(printerManager);
    }

    public void createPrint(String name, String filename, int height, int width, int length, ArrayList<Integer> filamentLength) {
        Print p = new Print(name, filename, height, width, length, filamentLength);
        printerManager.addToPrintList(p);
    }
}
