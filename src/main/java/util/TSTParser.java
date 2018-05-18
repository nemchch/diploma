package util;

import data.Action;
import data.Label;
import data.ReceiveAction;
import data.SendAction;
import exception.IncorrectActionException;
import exception.IncorrectTimeoutException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TSTParser {

    public static List<Action> parse(String globalAction) throws IncorrectActionException {
        List<Action> actionList = new ArrayList<>();
        globalAction = validate(globalAction);
        String[] actions = globalAction.split("\\.");
        for (String action : actions) {
            if (action.startsWith("!")) {
                SendAction sendAction = new SendAction(setParameters(action));
                actionList.add(sendAction);
            } else {
                if (action.startsWith("?")) {
                    ReceiveAction receiveAction = new ReceiveAction(setParameters(action));
                    actionList.add(receiveAction);
                } else {
                    throw new IncorrectActionException();
                }
            }
        }
        actionList = validateTime(actionList);
        return actionList;
    }

    private static List<Action> validateTime(List<Action> actionList) {
        Action action;
        for (int i=1;i<actionList.size();++i) {
            action = actionList.get(i);
            int time = actionList.get(i).getTime();
            for (int j=0; j<i;++j) {
                time -= actionList.get(j).getTime();
            }
            action.setTime(time);
            actionList.set(i, action);
        }
        return actionList;
    }


    @NotNull
    private static String validate(@NotNull String string) {
        return string.replaceAll(" ", "").toLowerCase();
    }

    private static Action setParameters(String string) {
        Action action = new Action();
        try {
            action.setLabel(Label.valueOf(string.substring(1, string.indexOf("{"))));
        } catch (Exception e) {
            action.setLabel(null);
        }
        try {
            int time = Integer.parseInt(string.substring(string.indexOf("<") + 1, string.indexOf("}")));
            if (time >= 0) {
                action.setTime(time);
            } else {
                exit(action);
            }
        } catch (Exception e) {
            action.setTime(0);
        }
        return action;
    }
    private static void exit(Action action) {
        try {
            throw new IncorrectTimeoutException("Timeout for action \"" + action.getLabel() + "\" is incorrect.");
        } catch (IncorrectTimeoutException ignored) {
        }
        System.exit(1);
    }
}