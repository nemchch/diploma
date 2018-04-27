package data;

import org.jetbrains.annotations.NotNull;

public class ReceiveAction extends Action {
    public ReceiveAction(@NotNull Action action) {
        this.setTime(action.getTime());
        this.setLabel(action.getLabel());
    }
}
