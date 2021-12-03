package nl.saxion.Models.prints.state;

import nl.saxion.Models.prints.PrintTask;

public abstract class PrintTaskState {
    protected final PrintTask task;

    public PrintTaskState(PrintTask task) {
        this.task = task;
    }

    // one function when state is enabled
    public abstract void stateEnabled();
}
