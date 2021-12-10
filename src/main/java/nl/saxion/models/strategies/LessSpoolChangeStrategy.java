package nl.saxion.models.strategies;

import nl.saxion.models.printers.Printer;
import nl.saxion.models.prints.Print;
import nl.saxion.models.prints.PrintTask;
import nl.saxion.models.prints.Spool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessSpoolChangeStrategy implements PrintStrategy {
    @Override
    public PrintTask selectPrintTask(List<PrintTask> pendingPrintTasks, Printer printer) {
        List<PrintTask> compatibleTasks = new ArrayList<>();
        Spool[] spools = printer.getCurrentSpools();
        PrintTask chosenTask;

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
            chosenTask = compatibleTasks.get(0);
            for (PrintTask pendingPrintTask : compatibleTasks) {
                System.out.println(pendingPrintTask);
            }

            System.out.println("chosen task: " + chosenTask);
        } else {
            List<PrintTask> availablePendingTasks = pendingPrintTasks;
            Collections.sort(availablePendingTasks);
            chosenTask = availablePendingTasks.get(0);

            Spool chosenSpool = null;
            for (Spool spool : spools) {
                if (spool.spoolMatch(chosenTask.getColors().get(0), chosenTask.getFilamentType())) {
                    if (spool.getLength() >= chosenTask.getPrint().getLength()) {
                        chosenSpool = spool;
                        break;
                    }
                }
            }
            System.out.println("chosen spool: " + chosenSpool);

            if (chosenSpool != null) {
                printer.setCurrentSpool(chosenSpool);
            }
        }

        return chosenTask;
    }

    @Override
    public String toString() {
        return "Less Spool Changes";
    }
}
