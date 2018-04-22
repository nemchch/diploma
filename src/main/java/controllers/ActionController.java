package controllers;

import data.Action;
import data.Label;
import exceptions.IllegalActionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import server.TcpEchoServer;
import services.UserActivityService;
import utils.TSTParser;

import java.util.List;

@Controller
public class ActionController {

    @Autowired
    UserActivityService userActivityService;

    public static void main(String[] args) {
        String string =  "?connect{t<10} . !login{t<30}. !password{t<40} .! send {t<30} .! disconnect{t<10}";
        List<Action> actions = null;
        try {
            actions = TSTParser.parse(string);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        TcpEchoServer server = new TcpEchoServer();
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
