package nl.saxion.models.prints;

import nl.saxion.models.prints.state.PendingState;
import nl.saxion.models.prints.state.PrintTaskState;

import java.util.List;

public class PrintTask {
    private Print print;
    private List<String> colors;
    private FilamentType filamentType;
    private PrintTaskState state;

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
        this.state.stateEnabled();
    }

    public void completeTask() {
        this.state.stateEnabled();
    }

    public void failTask() {
        this.state.stateEnabled();
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
