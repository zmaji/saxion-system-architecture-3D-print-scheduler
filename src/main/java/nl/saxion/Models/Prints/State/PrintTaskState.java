package nl.saxion.models.prints.state;

import nl.saxion.models.prints.PrintTask;

public abstract class PrintTaskState {
    protected final PrintTask task;

    public PrintTaskState(PrintTask task) {
        this.task = task;
    }

    // one function when state is enabled
    public abstract void stateEnabled();
}
