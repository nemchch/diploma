package diploma.server;

import diploma.services.impl.UserActivityServiceImpl;
import org.jetbrains.annotations.Contract;
import diploma.services.UserActivityService;

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
    private long userActivityId;

    private UserActivityService userActivityService = new UserActivityServiceImpl();

    public TcpEchoServer() {
        try {
            setServer(new ServerSocket(PORT));
        } catch (Exception ex) {
            System.err.println("Could not listen on port: " + PORT);
        }
    }

    public void disconnect() {
        try {
            String time = new Date().toString();
            userActivityService.disconnected(userActivityId, time);
            serverSocket.close();
            clientSocket.close();
            System.out.println("TCP Echo Server Stopped on port " + PORT);
        } catch (Exception e) {
            System.out.println("Error in stop server socket " + e);
        }
    }

    public void connect() {
        try {
            System.out.println("TCP Echo Server Started on port " + PORT + ".\nWaiting for connection.....");
            clientSocket = serverSocket.accept();
            System.out.println("Client Connection successful\nWaiting for input.....");
        } catch (IOException e) {
            System.out.println("Exception in echo server.\nExpected when shutdown. {}" + e.getLocalizedMessage());
        }
    }

    public void send() {
        try {
            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.write((user + ": ").getBytes());
            String inputLine;
            inputLine = in.readLine();
            System.out.println("Server: " + inputLine);
            if (inputLine.equals("quit")) {
                out.close();
                clientSocket.close();
                in.close();
            }
        } catch (IOException e) {
            System.out.println("Exception in echo server.\nExpected when shutdown. {}" + e.getLocalizedMessage());
        }
    }

    public void login() {
        try {
            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.write("login: ".getBytes());
            String login = in.readLine();
            System.out.println("Login is " + login);
            user = login;
        } catch (IOException e) {
            System.out.println("Incorrect login. Server will be stop. Please, try again");
        }
    }

    public  void password() {
        try {
            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.write("password: ".getBytes());
            String password = in.readLine();
            System.out.println("Password is " + password);
            String time = new Date().toString();
            out.write(("Successful authorization for user " + user + " in " + time + "\r\n\r\n").getBytes());
            userActivityId = userActivityService.connected(user, time);
        } catch (IOException e) {
            System.out.println("Incorrect password. Server will be stop. Please, try again");
        }
    }

    @Contract(pure = true)
    private ServerSocket getServer() {
        return serverSocket;
    }

    private void setServer(ServerSocket server) {
        this.serverSocket = server;
    }
}