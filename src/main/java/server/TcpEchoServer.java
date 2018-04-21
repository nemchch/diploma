package server;

import org.jetbrains.annotations.Contract;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpEchoServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private static final int PORT = 3345;

    public TcpEchoServer() {
        try {
            setServer(new ServerSocket(PORT));
        } catch (Exception ex) {
            System.err.println("Could not listen on port: " + PORT);
        }
    }

    public void disconnect() {
        try {
            serverSocket.close();
            System.out.println("TCP Echo Server Stopped on port " + PORT);
        } catch (Exception e) {
            System.out.println("error in stop server socket " + e);
        }
    }

    public void connect() throws IOException {
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
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server: " + inputLine);
                if (inputLine.equals("quit")) {
                    out.close();
                    clientSocket.close();
                    in.close();
                    break;
                }
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
        } catch (IOException e) {
            System.out.println("Incorrect login. Server will be stop. Please, try again");
        }
    }

    public void password() {
        try {
            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.write("password: ".getBytes());
            String password = in.readLine();
            System.out.println("Password is " + password);
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