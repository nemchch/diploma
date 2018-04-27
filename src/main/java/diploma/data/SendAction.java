package diploma.data;

import org.jetbrains.annotations.NotNull;

public class SendAction extends Action {
    public SendAction(@NotNull Action action) {
        this.setTime(action.getTime());
        this.setLabel(action.getLabel());
    }
}
