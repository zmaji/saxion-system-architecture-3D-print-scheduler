package nl.saxion.Models.prints.observer;

import nl.saxion.Models.prints.PrintTask;

public interface PrintTaskObserver {
    public void update(PrintTask task);
}
