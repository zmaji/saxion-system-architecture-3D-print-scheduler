package nl.saxion.models.prints.state;

import nl.saxion.models.prints.PrintTask;

public class CompletedState extends PrintTaskState {
    public CompletedState(PrintTask task) {
        super(task);
    }

    @Override
    public void stateEnabled() {
        System.out.println("PrintTask has been completed successfully");
    }
}
