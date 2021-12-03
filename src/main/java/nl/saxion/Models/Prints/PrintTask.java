package nl.saxion.Models.Prints;

import nl.saxion.Models.Prints.Observer.PrintTaskObserver;
import nl.saxion.Models.Prints.state.CompletedState;
import nl.saxion.Models.Prints.state.FailedState;
import nl.saxion.Models.Prints.state.PendingState;
import nl.saxion.Models.Prints.state.PrintTaskState;

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
        this.state.reqisterPending();
    }

    public void completeTask() {
        this.state.registerCompletion();
    }

    public void failTask() {
        this.state.registerFailure();
    }

    public void setState(PrintTaskState state) {
        this.state = state;
    }

    public PrintTaskState getState() {
        return state;
    }

    @Override
    public String toString() {
        return print.getName() +" " + filamentType + " " + colors.toString();
    }
}
