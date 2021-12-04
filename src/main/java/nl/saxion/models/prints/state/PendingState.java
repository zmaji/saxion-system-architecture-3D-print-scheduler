package nl.saxion.models.prints.state;

import nl.saxion.models.prints.PrintTask;

public class PendingState extends PrintTaskState {
    public PendingState(PrintTask task) {
        super(task);
    }

    @Override
    public void stateEnabled() {
        System.out.println("Print is waiting to be printed.");
    }
}
