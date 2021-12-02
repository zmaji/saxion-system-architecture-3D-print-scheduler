package nl.saxion.Models.Prints.state;

import nl.saxion.Models.Prints.PrintTask;

public class PendingState extends PrintTaskState {
    public PendingState(PrintTask task) {
        super(task);
    }

    @Override
    public void reqisterPending() {
        System.out.println("Print is waiting to be printed.");
    }

    @Override
    public void registerCompletion() {
        System.out.println("Print has yet to be printed.");
    }

    @Override
    public void registerFailure() {
        System.out.println("Print has yet to be printed.");
    }
}
