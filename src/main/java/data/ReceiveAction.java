package data;

public class ReceiveAction extends Action {
    public ReceiveAction(Action action) {
        this.setTime(action.getTime());
        this.setLabel(action.getLabel());
    }
}
