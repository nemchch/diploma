package server;

import org.jetbrains.annotations.Contract;
import services.UserActivityService;
import services.UserService;
import services.impl.UserActivityServiceImpl;
import services.impl.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TcpEchoServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private final int PORT = 3345;
    private String user;
    private long userActivityId = 0;

    private UserActivityService userActivityService = new UserActivityServiceImpl();
    private UserService userService = new UserServiceImpl();

    public TcpEchoServer() {
        try {
            setServer(new ServerSocket(PORT));
        } catch (Exception ex) {
            System.err.println("Could not listen on port: " + PORT + ".");
        }
    }

    public void writeError() {
        try {
            OutputStream out = clientSocket.getOutputStream();
            out.write(("\r\n\r\nTimeout expired.\r\n").getBytes());
        } catch (IOException ignored) {
        }
    }

    public void disconnect() {
        try {
            OutputStream out = clientSocket.getOutputStream();
            out.write(("\r\nConnection completed.").getBytes());
            if (userActivityId != 0) {
                String time = new Date().toString();
                userActivityService.disconnected(userActivityId, time);
            }
            serverSocket.close();
            clientSocket.close();
            System.out.println("TCP Echo Server Stopped on port " + PORT + ".");
            System.exit(0);
        } catch (Exception ignored) {
        }
    }

    public void initialize() {
        System.out.println("TCP Echo Server Started on port " + PORT + ".\nWaiting for connection.....");

    }

    public void connect() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Client Connection successful.\nWaiting for input.....");
        } catch (IOException e) {
            disconnect();
        }
    }

    public boolean send() throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.write(("\r\n"+ user + ": ").getBytes());
        String command;
        command = in.readLine();
        if (command.equals("show running-config")) {
            out.write(("\r\nBuilding configuration...\r\n").getBytes());
            /**/
            out.write(("\r\ninterface FastEthernet0/0\r\n" +
                    "description \"LAN1\"\r\n" +
                    "ip address 192.168.0.1 255.255.255.0\r\n" +
                    "end\r\n").getBytes());
        } else {
            System.err.println("\nIncorrect command. Access denied.\n");
            out.write(("\r\nIncorrect command. Access denied.\r\n").getBytes());
            disconnect();
            return false;
        }
        return true;
    }

    public boolean login() throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        assert out != null;
        out.write("login: ".getBytes());
        String login = in.readLine();
        if (isLoginCorrect(login)) {
            System.out.println("Login is " + login + ".");
            user = login;
            return true;
        } else {
            System.err.println("\nIncorrect login. Access denied.\n");
            out.write(("\r\nIncorrect login. Access denied.\r\n").getBytes());
            disconnect();
            return false;
        }
    }

    public boolean password() throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.write("password: ".getBytes());
        String password = in.readLine();
        if (isPasswordCorrect(user, password)) {
            System.out.println("Password is " + password + ".");
            String time = new Date().toString();
            out.write(("Successful authorization for user " + user + " in " + time + ".\r\n").getBytes());
            userActivityId = userActivityService.connected(user, time);
        } else {
            out.write("Incorrect password. Please, try again.\r\npassword: ".getBytes());
            password = in.readLine();
            if (isPasswordCorrect(user, password)) {
                System.out.println("Password is " + password + ".");
                String time = new Date().toString();
                out.write(("Successful authorization for user " + user + " in " + time + ".\r\n").getBytes());
                userActivityId = userActivityService.connected(user, time);
            } else {
                out.write("Incorrect password. Please, try again.\r\npassword: ".getBytes());
                password = in.readLine();
                if (isPasswordCorrect(user, password)) {
                    System.out.println("Password is " + password + ".");
                    String time = new Date().toString();
                    out.write(("Successful authorization for user " + user + " in " + time + ".\r\n").getBytes());
                    userActivityId = userActivityService.connected(user, time);
                } else {
                    System.err.println("\nIncorrect password. Access denied.\n");
                    out.write(("\r\nIncorrect password. Access denied.\r\n").getBytes());
                    disconnect();
                    return false;
                }
            }
        }
        return true;
    }

    @Contract(pure = true)
    private ServerSocket getServer() {
        return serverSocket;
    }

    private boolean isPasswordCorrect(String login, String password) {
        String realPassword = userService.getPassword(login);
        return realPassword.equals(password);
    }

    private boolean isLoginCorrect(String login) {
        return userService.isLogin(login);
    }

    private void setServer(ServerSocket server) {
        this.serverSocket = server;
    }
}