package utils;

import data.Action;
import data.Label;
import data.ReceiveAction;
import data.SendAction;
import exceptions.IllegalActionException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TSTParser {

    public List<Action> parse(String globalAction) throws IllegalActionException {
        List<Action> actionList = new ArrayList<>();
        globalAction = validate(globalAction);
        String[] actions = globalAction.split("\\.");
        for (String action : actions) {
            if (action.startsWith("!")) {
                SendAction sendAction = (SendAction) setParameters(action);
                actionList.add(sendAction);
            } else {
                if (action.startsWith("?")) {
                    ReceiveAction receiveAction = (ReceiveAction) setParameters(action);
                    actionList.add(receiveAction);
                } else {
                    throw new IllegalActionException();
                }
            }
        }
        return actionList;
    }

    @NotNull
    private String validate(String string) {
        return string.replaceAll(" ", "").toLowerCase();
    }

    private Action setParameters(String string) {
        Action action = new Action();
        try {
            action.setLabel(Label.valueOf(string.substring(1, string.indexOf("{"))));
        } catch (Exception e) {
            action.setLabel(null);
        }
        try {
            action.setTime(Integer.parseInt(string.substring(string.indexOf("<") + 1, string.indexOf("}"))));
        } catch (Exception e) {
            action.setTime(0);
        }
        return action;
    }
}