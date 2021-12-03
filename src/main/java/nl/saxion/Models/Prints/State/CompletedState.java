package nl.saxion.Models.Prints.state;

import nl.saxion.Models.Prints.PrintTask;

public class CompletedState extends PrintTaskState {
    public CompletedState(PrintTask task) {
        super(task);
    }

    @Override
    public void stateEnabled() {
        System.out.println("PrintTask has been completed successfully");
    }
}
