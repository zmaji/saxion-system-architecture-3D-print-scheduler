package nl.saxion.Models.Prints;

import nl.saxion.Models.Prints.Observer.PrintTaskObserver;
import nl.saxion.Models.Prints.State.CompletedState;
import nl.saxion.Models.Prints.State.FailedState;
import nl.saxion.Models.Prints.State.PendingState;
import nl.saxion.Models.Prints.State.PrintTaskState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class PrintTask {
    private Print print;
    private List<String> colors;
    private FilamentType filamentType;
    private PrintTaskState state;
    private List<PrintTaskObserver> observers = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    public PrintTask(Print print, List<String> colors, FilamentType filamentType){
        this.print = print;
        this.colors = colors;
        this.filamentType = filamentType;
        state = new PendingState(this);
    }

    public List<String> getColors() {
        return colors;
    }

    public FilamentType getFilamentType() {
        return filamentType;
    }

    public Print getPrint(){
        return print;
    }

    public void setTaskPending() {
        this.state = new PendingState(this);
        this.state.reqisterPending();
    }

    public void completeTask() {
        this.state = new CompletedState(this);
        this.state.registerCompletion();
    }

    public void failTask() {
        this.state = new FailedState(this);
        this.state.registerFailure();
    }

    public void subscribeObserver(PrintTaskObserver observer) {
        observers.add(observer);
    }

    public void unsubscribeObserver(PrintTaskObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (PrintTaskObserver observer : observers) {
            observer.notify();
        }
    }

    public PrintTaskState getState() {
        return state;
    }

    @Override
    public String toString() {
        return print.getName() +" " + filamentType + " " + colors.toString();
    }
}
