package controller;

import co2api.TST;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import data.Action;
import data.Label;
import exception.IncorrectActionException;
import server.TcpEchoServer;
import util.TSTParser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static util.ProtocolsLoader.getProtocols;

public class ActionController {
    public static void main(String[] args) {
        String clientProtocolString = "";
        String serverProtocolString = "";
        TST clientProtocol = null;
        TST serverProtocol = null;
        int addedTime = 0;
        long start;
        long finish;
        Map<String, String> protocolsMap = getProtocols();
        if (protocolsMap.get("clientProtocol") != null) {
            clientProtocolString = protocolsMap.get("clientProtocol");
        } else {
            System.err.println("\nClient protocol is not present.\n");
            System.exit(1);
        }
        if (protocolsMap.get("serverProtocol") != null) {
            serverProtocolString = protocolsMap.get("serverProtocol");
        } else {
            System.err.println("\nServer protocol is not present.\n");
            System.exit(1);
        }

        try {
            clientProtocol = new TST(clientProtocolString);
        } catch (Exception e) {
            try {
                throw new IncorrectActionException("Client");
            } catch (IncorrectActionException e1) {
                e1.getMessage();
            }
        }
        try {
            serverProtocol = new TST(serverProtocolString);
        } catch (Exception e) {
            try {
                throw new IncorrectActionException("Server");
            } catch (IncorrectActionException ignored) {
            }
        }
        assert serverProtocol != null;
        assert clientProtocol != null;
        if (serverProtocol.isCompliantWith(clientProtocol)) {
            TcpEchoServer server = new TcpEchoServer();
            List<Action> serverActions = null;
            List<Action> clientActions = null;
            TimeLimiter timeLimiter = new SimpleTimeLimiter();
            Action action = null;
            try {
                serverActions = TSTParser.parse(serverProtocolString);
            } catch (IncorrectActionException ignored) {
            }
            try {
                clientActions = TSTParser.parse(clientProtocolString);
            } catch (IncorrectActionException ignored) {
            }
            try {
                try {
                    server.initialize();
                } catch (Exception e) {
                    System.err.println("\nConnection failed.\n");
                    System.exit(1);
                }
                assert serverActions != null;
                action = getAction(serverActions, "connect");
                if (action == null) {
                    System.err.println("\nAction \"connect\" is not present. Protocol is unavailable.\n");
                    System.exit(1);
                } else {
                    Callable<Boolean> connectTask = server::connect;
                    start = System.currentTimeMillis();
                    timeLimiter.callWithTimeout(connectTask, action.getTime(), TimeUnit.SECONDS, true);
                    finish = System.currentTimeMillis();
                    addedTime += (finish - start) / 1000;
                }
                assert clientActions != null;
                action = getAction(clientActions, "login");
                if (action == null) {
                    System.err.println("\nAction \"login\" is not present. Protocol is unavailable.\n");
                    server.disconnect();
                } else {
                    Callable<Boolean> loginTask = server::login;
                    start = System.currentTimeMillis();
                    timeLimiter.callWithTimeout(loginTask, action.getTime() + addedTime, TimeUnit.SECONDS, true);
                    finish = System.currentTimeMillis();
                    addedTime += (finish - start) / 1000;
                }
                action = getAction(clientActions, "password");
                if (action == null) {
                    System.err.println("\nAction \"password\" is not present. Protocol is unavailable.\n");
                    server.disconnect();
                } else {
                    Callable<Boolean> passwordTask = server::password;
                    start = System.currentTimeMillis();
                    timeLimiter.callWithTimeout(passwordTask, action.getTime() + addedTime, TimeUnit.SECONDS, true);
                    finish = System.currentTimeMillis();
                    addedTime += (finish - start) / 1000;
                }
                action = getAction(clientActions, "send");
                if (action != null) {
                    Callable<Boolean> sendTask = server::send;
                    start = System.currentTimeMillis();
                    timeLimiter.callWithTimeout(sendTask, action.getTime() + addedTime, TimeUnit.SECONDS, true);
                    finish = System.currentTimeMillis();
                    addedTime += (finish - start) / 1000;
                }
                action = getAction(serverActions, "disconnect");
                if (action == null) {
                    System.err.println("\nAction \"disconnect\" is not present. Protocol is unavailable.\n");
                    server.disconnect();
                } else {
                    Callable<Boolean> disconnectTask = server::disconnect;
                    start = System.currentTimeMillis();
                    timeLimiter.callWithTimeout(disconnectTask, action.getTime() + addedTime, TimeUnit.SECONDS, true);
                    finish = System.currentTimeMillis();
                    System.out.println((finish - start) / 1000);
                }
            } catch (Exception e) {
                server.writeError();
                System.err.println("\nTimeout expired.\n");
                server.disconnect();
                System.exit(1);
            }
        } else {
            System.err.println("\nProtocols are not compliant.\n");
            System.exit(1);
        }
    }

    private static Action getAction(List<Action> actions, String label) {
        return actions.stream().filter(action -> action.getLabel().equals(Label.valueOf(label))).findFirst().orElse(null);
    }
}
