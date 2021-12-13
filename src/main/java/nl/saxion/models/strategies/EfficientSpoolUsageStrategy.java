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
    public List<PrintTask> selectPrintTask(List<PrintTask> pendingPrintTasks, Printer printer) {
        List<PrintTask> compatibleTasks = new ArrayList<>();
        List<Spool> spools = new ArrayList<>(Arrays.stream(printer.getCurrentSpools()).toList());
        Spool lowestSpool = spools.get(0);

        while (spools.size() != 0) {
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
                printer.setCurrentSpool(lowestSpool);
                break;
            } else if (compatibleTasks.size() > 1) {
                Collections.sort(compatibleTasks);
                printer.setCurrentSpool(lowestSpool);
                break;
            } else {
                spools.remove(lowestSpool);
                if (spools.size() == 0) {
                    break;
                }
            }
        }

        return compatibleTasks;
    }

    @Override
    public String toString() {
        return "Efficient Spool Usage";
    }
}
