package nl.saxion.Models.Prints.state;

import nl.saxion.Models.Prints.PrintTask;

public class FailedState extends PrintTaskState {
    public FailedState(PrintTask task) {
        super(task);
    }

    @Override
    public void stateEnabled() {
        System.out.println("Print failed, retrying...");
    }
}
