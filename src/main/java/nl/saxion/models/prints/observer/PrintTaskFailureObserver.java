package nl.saxion.models.prints.observer;

import nl.saxion.models.prints.PrintTask;

public class PrintTaskFailureObserver implements PrintTaskObserver {
    @Override
    public void update(PrintTask task) {
        System.out.println(task.getState());
    }
}
