package data;

public class SendAction extends Action {
    public SendAction(Action action) {
        this.setTime(action.getTime());
        this.setLabel(action.getLabel());
    }
}
