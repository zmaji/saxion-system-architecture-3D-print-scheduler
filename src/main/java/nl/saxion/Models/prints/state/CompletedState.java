package nl.saxion.Models.prints.state;

import nl.saxion.Models.prints.PrintTask;

public class CompletedState extends PrintTaskState {
    public CompletedState(PrintTask task) {
        super(task);
    }

    @Override
    public void stateEnabled() {
        System.out.println("PrintTask has been completed successfully");
    }
}
