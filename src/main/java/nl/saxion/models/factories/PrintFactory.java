package nl.saxion.models.factories;

import nl.saxion.models.managers.PrinterManager;
import nl.saxion.models.prints.Print;

import java.util.ArrayList;

public class PrintFactory extends PrintSchedulerFactory {

    public PrintFactory(PrinterManager printerManager) {
        super(printerManager);
    }

    /** Creates an instance of a Print based on given parameters from the Reader Class and adds it to a file managed by PrinterManager
     *
     * @param name the name of the Print
     * @param filename the filename of the Print
     * @param height the height of the Print
     * @param width the width of the Print
     * @param length the length of the Print
     * @param filamentLength the filamentLength of the Print
     */
    public void createPrint(String name, String filename, int height, int width, int length, ArrayList<Integer> filamentLength) {
        Print p = new Print(name, filename, height, width, length, filamentLength);
        printerManager.addToPrintList(p);
    }
}
