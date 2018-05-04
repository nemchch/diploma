package controller;

import co2api.TST;
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
        String clientProtocolString = "?connect{t<10} .!login{t<20}.!password{t<60}.!send{t<80} .? disconnect{t<90}";
        String serverProtocolString = "!connect{t<10} .?login{t<30}.?password{t<60} .?send{t<80}.! disconnect{t<90}";
        TST clientProtocol = new TST(clientProtocolString);
        TST serverProtocol = new TST(serverProtocolString);
        if (serverProtocol.isCompliantWith(clientProtocol)) {
            TcpEchoServer server = new TcpEchoServer();
            List<Action> serverActions = null;
            List<Action> clientActions = null;
            TimeLimiter timeLimiter = new SimpleTimeLimiter();
            Action action = null;
            try {
                serverActions = TSTParser.parse(serverProtocolString);
            } catch (IllegalActionException e) {
                e.printStackTrace();
            }
            try {
                clientActions = TSTParser.parse(clientProtocolString);
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
                assert serverActions != null;
                try {
                    action = getAction(serverActions, "connect");
                } catch (Exception e) {
                    System.err.println("Action \"connect\" is not present. Protocol is unavailable");
                    System.exit(1);
                }
                if (action != null) {
                    Callable<Boolean> connectTask = server::connect;
                    timeLimiter.callWithTimeout(connectTask, action.getTime(), TimeUnit.SECONDS, true);
                }
                try {
                    assert clientActions != null;
                    action = getAction(clientActions, "login");
                } catch (Exception e) {
                    System.err.println("Action \"login\" is not present. Protocol is unavailable");
                    server.disconnect();
                }
                if (action != null) {
                    Callable<Boolean> loginTask = server::login;
                    timeLimiter.callWithTimeout(loginTask, action.getTime(), TimeUnit.SECONDS, true);
                }


                try {
                    action = getAction(clientActions, "password");
                } catch (Exception e) {
                    System.err.println("Action \"password\" is not present. Protocol is unavailable");
                    server.disconnect();
                }
                if (action != null) {
                    Callable<Boolean> passwordTask = server::password;
                    timeLimiter.callWithTimeout(passwordTask, action.getTime(), TimeUnit.SECONDS, true);
                }
                try {
                    action = getAction(clientActions, "send");
                } catch (Exception ignored) {
                }
                if (action != null) {
                    Callable<Boolean> sendTask = server::send;
                    timeLimiter.callWithTimeout(sendTask, action.getTime(), TimeUnit.SECONDS, true);
                }
                try {
                    action = getAction(serverActions, "disconnect");
                } catch (Exception ignored) {
                }
                if (action != null) {
                    Callable<Boolean> disconnectTask = server::disconnect;
                    timeLimiter.callWithTimeout(disconnectTask, action.getTime(), TimeUnit.SECONDS, true);
                }
            } catch (Exception e) {
                server.writeError();
                System.err.println("\nTimeout expired.\n");
                server.disconnect();
                System.exit(1);
            }
        } else {
            System.err.println("Protocols are not compliant");
            System.exit(1);
        }
    }

    private static Action getAction(List<Action> actions, String label) {
        return actions.stream().filter(action -> action.getLabel().equals(Label.valueOf(label))).findFirst().orElse(null);
    }
}
