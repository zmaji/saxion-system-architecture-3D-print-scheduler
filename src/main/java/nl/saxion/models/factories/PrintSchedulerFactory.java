package nl.saxion.models.factories;

import nl.saxion.models.managers.PrinterManager;

public abstract class PrintSchedulerFactory {

    PrinterManager printerManager;

    public PrintSchedulerFactory(PrinterManager printerManager) {
        this.printerManager = printerManager;
    }
}
