package nl.saxion.Models.Prints.Observer;

import nl.saxion.Models.Prints.PrintTask;

public interface PrintTaskObserver {
    public void update(PrintTask task);
}
