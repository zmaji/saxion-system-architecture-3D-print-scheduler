package nl.saxion.models.prints.state;

import nl.saxion.models.prints.PrintTask;

public class FailedState extends PrintTaskState {
    public FailedState(PrintTask task) {
        super(task);
    }

    @Override
    public void stateEnabled() {
        System.out.println("Print failed, retrying...");
    }
}
