package nl.saxion.Models.prints.observer;

import nl.saxion.Models.prints.PrintTask;

public class PrintTaskFailureObserver implements PrintTaskObserver {
    @Override
    public void update(PrintTask task) {
        System.out.println(task.getState());
    }
}
