package nl.saxion.models.factories;

import nl.saxion.models.manager.PrinterManager;
import nl.saxion.models.printers.HousedPrinter;
import nl.saxion.models.printers.MultiColor;
import nl.saxion.models.printers.StandardFDM;
import nl.saxion.models.prints.Spool;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class PrinterFactory {

    PrinterManager printerManager;

    public PrinterFactory(PrinterManager printerManager) {
        this.printerManager = printerManager;
    }

    public void createPrinter(int id, int printerType, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, JSONArray currentSpools) {
        switch(printerType) {
            case 1 -> addStandardFDMPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, currentSpools);
            case 2 -> addHousedPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, currentSpools);
            case 3 -> addMultiColorPrinter(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors, currentSpools);
        }
    }

    /** Creates a new StandardFDM Printer based on given parameters and adds it to certain lists
     *  @param id the ID value of the Printer
     * @param printerName the name of the Printer
     * @param manufacturer the manufacturer of the Printer
     * @param maxX the maxX value of the Printer
     * @param maxY the maxY value of the Printer
     * @param maxZ the maxZ value of the Printer
     * @param currentSpools the currentSpools of the Printer
     */
    private void addStandardFDMPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, JSONArray currentSpools) {
        StandardFDM printer = new StandardFDM(id, printerName, manufacturer, maxX, maxY, maxZ);
        Spool cspool = this.printerManager.getSpoolByID(((Long) currentSpools.get(0)).intValue());
        printer.setCurrentSpool(cspool);
        this.printerManager.removeFreeSpool(cspool);
        this.printerManager.addToPrinters(printer);
        this.printerManager.addFreePrinter(printer);
    }

    /** Creates a new HousedPrinter based on given parameters and adds it to certain lists
     *  @param id the ID value of the Printer
     * @param printerName the name of the Printer
     * @param manufacturer the manufacturer of the Printer
     * @param maxX the maxX value of the Printer
     * @param maxY the maxY value of the Printer
     * @param maxZ the maxZ value of the Printer
     * @param currentSpools the currentSpools of the Printer
     */
    private void addHousedPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, JSONArray currentSpools) {
        HousedPrinter printer = new HousedPrinter(id, printerName, manufacturer, maxX, maxY, maxZ);
        Spool cspool = this.printerManager.getSpoolByID(((Long) currentSpools.get(0)).intValue());
        printer.setCurrentSpool(cspool);
        this.printerManager.removeFreeSpool(cspool);
        this.printerManager.addToPrinters(printer);
        this.printerManager.addFreePrinter(printer);
    }

    /** Creates a new MultiColor Printer based on given parameters and adds it to certain lists
     *  @param id the ID value of the Printer
     * @param printerName the name of the Printer
     * @param manufacturer the manufacturer of the Printer
     * @param maxX the maxX value of the Printer
     * @param maxY the maxY value of the Printer
     * @param maxZ the maxZ value of the Printer
     * @param maxColors the maximum colors of the Printer
     * @param currentSpools the currentSpools of the Printer
     */
    private void addMultiColorPrinter(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, JSONArray currentSpools) {
        MultiColor printer = new MultiColor(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors);
        ArrayList<Spool> cspools = new ArrayList<>();
        cspools.add(this.printerManager.getSpoolByID(((Long) currentSpools.get(0)).intValue()));
        cspools.add(this.printerManager.getSpoolByID(((Long) currentSpools.get(1)).intValue()));
        cspools.add(this.printerManager.getSpoolByID(((Long) currentSpools.get(2)).intValue()));
        cspools.add(this.printerManager.getSpoolByID(((Long) currentSpools.get(3)).intValue()));
        printer.setCurrentSpools(cspools);
        for(Spool spool: cspools) {
            this.printerManager.removeFreeSpool(spool);
        }
        this.printerManager.addToPrinters(printer);
        this.printerManager.addFreePrinter(printer);
    }
}
