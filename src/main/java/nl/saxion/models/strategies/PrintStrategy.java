package nl.saxion.models.strategies;

import nl.saxion.models.printers.Printer;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;

import java.util.List;

public interface PrintStrategy {
    public List<PrintTask> selectPrintTask(List<PrintTask> pendingPrintTasks, Printer printer);
    public String toString();
}
