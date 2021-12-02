package nl.saxion.Models.Prints.State;

import nl.saxion.Models.Prints.PrintTask;

public class CompletedState extends PrintTaskState {
    public CompletedState(PrintTask task) {
        super(task);
    }

    @Override
    public void reqisterPending() {
        System.out.println("PrintTask has already been completed");
    }

    @Override
    public void registerCompletion() {
        System.out.println("PrintTask has been completed successfully");
        task.notifyObservers();
    }

    @Override
    public void registerFailure() {
        System.out.println("PrintTask has already been completed");
    }
}
