package nl.saxion.models.factory;

import nl.saxion.models.manager.PrinterManager;

public abstract class PrintSchedulerFactory {

    PrinterManager printerManager;

    public PrintSchedulerFactory(PrinterManager printerManager) {
        this.printerManager = printerManager;
    }
}
