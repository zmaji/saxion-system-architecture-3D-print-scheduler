package nl.saxion.models.strategies;

import nl.saxion.models.printers.Printer;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessSpoolChangeStrategy implements PrintStrategy {
    @Override
    public List<PrintTask> selectPrintTask(List<PrintTask> pendingPrintTasks, Printer printer) {
        List<PrintTask> compatibleTasks = new ArrayList<>();
        Spool[] spools = printer.getCurrentSpools();

        for (PrintTask pendingPrintTask : pendingPrintTasks) {
            if (spools[0].spoolMatch(pendingPrintTask.getColors().get(0), pendingPrintTask.getFilamentType())) {
                compatibleTasks.add(pendingPrintTask);
            }
        }

        for (PrintTask pendingPrintTask : compatibleTasks) {
            System.out.println(pendingPrintTask);
        }

        if (compatibleTasks.size() != 0) {
            Collections.sort(compatibleTasks);
        } else {
            List<PrintTask> availablePendingTasks = pendingPrintTasks;
            Collections.sort(availablePendingTasks);
            PrintTask chosenTask = availablePendingTasks.get(0);

            Spool chosenSpool = null;
            for (Spool spool : spools) {
                if (spool.spoolMatch(chosenTask.getColors().get(0), chosenTask.getFilamentType())) {
                    if (spool.getLength() >= chosenTask.getPrint().getLength()) {
                        chosenSpool = spool;
                        break;
                    }
                }
            }

            if (chosenSpool != null) {
                printer.setCurrentSpool(chosenSpool);
                compatibleTasks.add(chosenTask);
            }
        }

        return compatibleTasks;
    }

    @Override
    public String toString() {
        return "Less Spool Changes";
    }
}
