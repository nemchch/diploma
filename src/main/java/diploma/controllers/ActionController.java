package diploma.controllers;

import diploma.data.Action;
import diploma.data.Label;
import diploma.exceptions.IllegalActionException;
import diploma.server.TcpEchoServer;
import diploma.utils.TSTParser;

import java.util.List;

public class ActionController {
    public static void main(String[] args) {
        String string = "?connect{t<10} . !login{t<30}. !password{t<40} .! send {t<30} .! disconnect{t<10}";
        TcpEchoServer server = new TcpEchoServer();
        List<Action> actions = null;
        try {
            actions = TSTParser.parse(string);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        assert actions != null;
        actions.forEach(action -> {
            if (action.getLabel().equals(Label.connect)) {
                try {
                    server.connect();
                } catch (Exception e) {
                    System.err.println("Couldn't start server:\n" + e);
                }
            }
            if (action.getLabel().equals(Label.disconnect)) {
                server.disconnect();
            }
            if (action.getLabel().equals(Label.login)) {
                server.login();
            }
            if (action.getLabel().equals(Label.password)) {
                server.password();
            }
            if (action.getLabel().equals(Label.send)) {
                server.send();
            }
        });
    }
}
