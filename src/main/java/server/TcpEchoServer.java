package server;

import org.jetbrains.annotations.Contract;
import service.UserActivityService;
import service.UserConfigService;
import service.UserService;
import service.impl.UserActivityServiceImpl;
import service.impl.UserConfigServiceImpl;
import service.impl.UserServiceImpl;

import java.io.*;
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
    private UserConfigService userConfigService = new UserConfigServiceImpl();

    public TcpEchoServer() {
        try {
            setServer(new ServerSocket(PORT));
        } catch (Exception ex) {
            System.err.println("\nCould not listen on port: " + PORT + ".\n");
            System.exit(1);
        }
    }

    public void writeError() {
        try {
            if (clientSocket != null) {
                OutputStream out = clientSocket.getOutputStream();
                out.write(("\r\n\r\nTimeout expired.\r\n").getBytes());
            }
        } catch (IOException ignored) {
        }
    }

    public boolean disconnect() {
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
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public void initialize() {
        System.out.println("TCP Echo Server Started on port " + PORT + ".\nWaiting for connection.....");

    }

    public boolean connect() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Client Connection successful.\nWaiting for input.....");
            return true;
        } catch (IOException e) {
            disconnect();
            return false;
        }
    }

    public boolean send() throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.write(("\r\n" + user + ": ").getBytes());
        String command;
        command = in.readLine();
        if (command.equals("show running-config")) {
            out.write(("\r\nBuilding configuration...\r\n\r\n").getBytes());
            String config = userConfigService.getConfig(userService.getId(user));
            out.write((config).getBytes());
        } else {
            if (command.equals("quit")) {
                disconnect();
            } else {
                System.err.println("\nIncorrect command. Access denied.\n");
                out.write(("\r\nIncorrect command. Access denied.\r\n").getBytes());
                disconnect();
                return false;
            }
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
        //out.write(new byte[]{(byte) 0xFF, (byte) 0xFE, 0x01});
        out.write("password: ".getBytes());
        String password = in.readLine()/*.substring(3)*/;
        if (isPasswordCorrect(user, password)) {
            String time = new Date().toString();
            out.write(("Successful authorization for user " + user + " in " + time + ".\r\n").getBytes());
            userActivityId = userActivityService.connected(user, time);
        } else {
            out.write("Incorrect password. Please, try again.\r\npassword: ".getBytes());
            password = in.readLine();
            if (isPasswordCorrect(user, password)) {
                String time = new Date().toString();
                out.write(("Successful authorization for user " + user + " in " + time + ".\r\n").getBytes());
                userActivityId = userActivityService.connected(user, time);
            } else {
                out.write("Incorrect password. Please, try again.\r\npassword: ".getBytes());
                password = in.readLine();
                if (isPasswordCorrect(user, password)) {
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

        //out.write(new byte[]{(byte) 0xFF, (byte) 0xFD, 0x01});
        return true;
    }

    @Contract(pure = true)
    private ServerSocket getServer() {
        return serverSocket;
    }

    private boolean isPasswordCorrect(String login, String password) {
        return userService.isPassword(login, password);
    }

    private boolean isLoginCorrect(String login) {
        return userService.isLogin(login);
    }

    private void setServer(ServerSocket server) {
        this.serverSocket = server;
    }
}