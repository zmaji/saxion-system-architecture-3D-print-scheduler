package nl.saxion.Models.prints.state;

import nl.saxion.Models.prints.PrintTask;

public class FailedState extends PrintTaskState {
    public FailedState(PrintTask task) {
        super(task);
    }

    @Override
    public void stateEnabled() {
        System.out.println("Print failed, retrying...");
    }
}
