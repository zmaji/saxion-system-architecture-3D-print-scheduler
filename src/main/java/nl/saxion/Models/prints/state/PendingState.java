package nl.saxion.Models.prints.state;

import nl.saxion.Models.prints.PrintTask;

public class PendingState extends PrintTaskState {
    public PendingState(PrintTask task) {
        super(task);
    }

    @Override
    public void stateEnabled() {
        System.out.println("Print is waiting to be printed.");
    }
}
