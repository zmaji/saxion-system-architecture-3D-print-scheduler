package nl.saxion.Models.Prints.state;

import nl.saxion.Models.Prints.PrintTask;

public abstract class PrintTaskState {
    protected final PrintTask task;

    public PrintTaskState(PrintTask task) {
        this.task = task;
    }

    // one function when state is enabled
    public abstract void stateEnabled();
}
