package nl.saxion.models.strategies;

import nl.saxion.models.printers.Printer;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;

import java.util.List;

public class EfficientSpoolUsageStrategy implements PrintStrategy {
    @Override
    public void calculatePrintTime() {
        System.out.println("Calculated time with the use of efficient spool strategy: 75");
    }

    @Override
    public void calculateTotalCost() {
        System.out.println("Calculated cost with the use of efficient spool strategy: 50");
    }

    @Override
    public PrintTask selectPrintTask(List<PrintTask> pendingPrintTasks, Printer printer) {
        return null;
    }

    @Override
    public String toString() {
        return "Efficient Spool Usage";
    }
}
