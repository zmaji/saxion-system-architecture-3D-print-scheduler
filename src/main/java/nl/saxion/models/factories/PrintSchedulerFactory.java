package nl.saxion.models.factories;

import nl.saxion.models.manager.PrinterManager;

public abstract class PrintSchedulerFactory {

    PrinterManager printerManager;

    public PrintSchedulerFactory(PrinterManager printerManager) {
        this.printerManager = printerManager;
    }
}
