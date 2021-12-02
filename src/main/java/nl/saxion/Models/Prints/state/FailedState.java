package nl.saxion.Models.Prints.state;

import nl.saxion.Models.Prints.PrintTask;

public class FailedState extends PrintTaskState {
    public FailedState(PrintTask task) {
        super(task);
    }

    @Override
    public void reqisterPending() {
        System.out.println("PrintTask has already been completed");
    }

    @Override
    public void registerCompletion() {
        System.out.println("PrintTask has failed and is not completed");
    }

    @Override
    public void registerFailure() {
        System.out.println("Print failed, retrying...");
        // observer takes over
//        task.notifyObservers();
        task.setTaskPending();
    }
}
