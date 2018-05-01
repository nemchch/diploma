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
        String string = "?connect{t<10} . !login{t<30}. !password{t<30} .! send {t<60} .! disconnect{t<10}";
        TcpEchoServer server = new TcpEchoServer();
        List<Action> actions = null;
        TimeLimiter timeLimiter = new SimpleTimeLimiter();
        Action action;
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
            }
            try {
                assert actions != null;
                action = getAction(actions, "connect");
                if (action != null) {
                    server.connect();
                }
            } catch (Exception e) {
                System.err.println("Couldn't connect to server:\n" + e);
            }
            action = getAction(actions, "login");
            Callable<Boolean> loginTask = server::login;
            timeLimiter.callWithTimeout(loginTask, action.getTime(), TimeUnit.SECONDS, true);

            action = getAction(actions, "password");
            Callable<Boolean> passwordTask = server::password;
            timeLimiter.callWithTimeout(passwordTask, action.getTime(), TimeUnit.SECONDS, true);

            action = getAction(actions, "send");
            Callable<Boolean> sendTask = server::send;
            timeLimiter.callWithTimeout(sendTask, action.getTime(), TimeUnit.SECONDS, true);

            action = getAction(actions, "disconnect");
            if (action != null) {
                server.disconnect();
            }
        } catch (Exception e) {
            server.writeError();
            System.err.println("\r\nTimeout expired\r\n");
            server.disconnect();
        }
    }

    private static Action getAction(List<Action> actions, String label) {
        return actions.stream().filter(action -> action.getLabel().equals(Label.valueOf(label))).findFirst().orElse(null);
    }
}
