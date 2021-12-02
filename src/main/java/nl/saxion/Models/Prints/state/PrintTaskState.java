package nl.saxion.Models.Prints.state;

import nl.saxion.Models.Prints.PrintTask;

public abstract class PrintTaskState {
    protected final PrintTask task;

    public PrintTaskState(PrintTask task) {
        this.task = task;
    }

    public abstract void reqisterPending();
    public abstract void registerCompletion();
    public abstract void registerFailure();
}
