package nl.saxion.models.strategies;

import nl.saxion.models.printers.Printer;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EfficientSpoolUsageStrategy implements PrintStrategy {
    @Override
    public PrintTask selectPrintTask(List<PrintTask> pendingPrintTasks, Printer printer) {
        List<PrintTask> compatibleTasks = new ArrayList<>();
        List<Spool> spools = Arrays.stream(printer.getCurrentSpools()).toList();
        Spool lowestSpool = spools.get(0);
        PrintTask chosenTask = null;

        for (Spool spool : spools) {
            if (spool.getLength() < lowestSpool.getLength()) {
                lowestSpool = spool;
            }
        }

        for (PrintTask pendingPrintTask : pendingPrintTasks) {
            if (pendingPrintTask.getColors().contains(lowestSpool.getColor()) && lowestSpool.getLength() >= pendingPrintTask.getPrint().getLength()) {
                compatibleTasks.add(pendingPrintTask);
            }
        }

        if (compatibleTasks.size() == 1) {
            chosenTask = compatibleTasks.get(0);
            printer.setCurrentSpool(lowestSpool);
        } else if (compatibleTasks.size() > 1) {
            Collections.sort(compatibleTasks);
            chosenTask = compatibleTasks.get(0);
            printer.setCurrentSpool(lowestSpool);
        } else {
            spools.remove(lowestSpool);
        }


        return chosenTask;
    }

    @Override
    public String toString() {
        return "Efficient Spool Usage";
    }
}
