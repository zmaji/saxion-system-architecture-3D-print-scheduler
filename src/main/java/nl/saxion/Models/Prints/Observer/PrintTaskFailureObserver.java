package nl.saxion.Models.Prints.Observer;

import nl.saxion.Models.Prints.PrintTask;

public class PrintTaskFailureObserver implements PrintTaskObserver {
    @Override
    public void update(PrintTask task) {
        System.out.println(task.getState());
    }
}
