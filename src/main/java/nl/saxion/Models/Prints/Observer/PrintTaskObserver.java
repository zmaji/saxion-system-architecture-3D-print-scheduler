package nl.saxion.models.prints.observer;

import nl.saxion.models.prints.PrintTask;

public interface PrintTaskObserver {
    public void update(PrintTask task);
}
