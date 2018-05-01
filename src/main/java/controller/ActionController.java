package controller;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import data.Action;
import data.Label;
import exceptions.IllegalActionException;
import server.TcpEchoServer;
import utils.TSTParser;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ActionController {
    public static void main(String[] args) {
        String string = "?connect{t<10} . !login{t<-20}. !password{t<30} .! disconnect{t<10}";
        TcpEchoServer server = new TcpEchoServer();
        List<Action> actions = null;
        TimeLimiter timeLimiter = new SimpleTimeLimiter();
        Action action = null;
        try {
            actions = TSTParser.parse(string);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        try {
            try {
                server.initialize();
            } catch (Exception e) {
                System.err.println("Couldn't start server:\n" + e);
                System.exit(0);
            }
            assert actions != null;
            try {
                action = getAction(actions, "connect");
            } catch (Exception e) {
                System.err.println("Action \"connect\" is not present. Protocol is unavailable");
                System.exit(1);
            }
            if (action != null) {
                server.connect();
            }
            try {
                action = getAction(actions, "login");
            } catch (Exception e) {
                System.err.println("Action \"login\" is not present. Protocol is unavailable");
                server.disconnect();
            }
            if (action != null) {
                Callable<Boolean> loginTask = server::login;
                timeLimiter.callWithTimeout(loginTask, action.getTime(), TimeUnit.SECONDS, true);
            }


            try {
                action = getAction(actions, "password");
            } catch (Exception e) {
                System.err.println("Action \"password\" is not present. Protocol is unavailable");
                server.disconnect();
            }
            if (action != null) {
                Callable<Boolean> passwordTask = server::password;
                timeLimiter.callWithTimeout(passwordTask, action.getTime(), TimeUnit.SECONDS, true);
            }
            try {
                action = getAction(actions, "send");
            } catch (Exception ignored) {
            }
            if (action != null) {
                Callable<Boolean> sendTask = server::send;
                timeLimiter.callWithTimeout(sendTask, action.getTime(), TimeUnit.SECONDS, true);
            }
            try {
                action = getAction(actions, "disconnect");
            } catch (Exception ignored) {
            }
            if (action != null) {
                server.disconnect();
            }
        } catch (Exception e) {
            server.writeError();
            System.err.println("\nTimeout expired.\n");
            server.disconnect();
        }
    }

    private static Action getAction(List<Action> actions, String label) {
        return actions.stream().filter(action -> action.getLabel().equals(Label.valueOf(label))).findFirst().orElse(null);
    }
}
