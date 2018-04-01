import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpEchoServer {

    private ServerSocket serverSocket;
    private int port = 3345;


    private TcpEchoServer() {
        try {
            setServer(new ServerSocket(port));
        } catch (Exception ex) {
            System.err.println("Could not listen on port: " + port);
        }
    }


    public static void main(String[] args) {
        try {
            TcpEchoServer server = new TcpEchoServer();
            server.serve();


        } catch (Exception e) {
            System.err.println("Couldn't start server:\n" + e);
        }
    }

    public void stop() {
        try {

            serverSocket.close();
        } catch (Exception e) {
            System.out.println("error in stop server socket " + e);
        }
    }

    private void serve() throws IOException {

        Socket clientSocket;
        try {
            while (true) {

                System.out.println("TCP Echo Server Started on port "
                        + port
                        + " . \nWaiting for connection.....");
                clientSocket = serverSocket.accept();
                System.out.println("Client Connection successful");
                System.out.println("Waiting for input.....");

                PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));

                String inputLine;
                //just read 1 single line then auto close
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Server: " + inputLine);
                    if (inputLine.equals("quit")) {
                        out.close();
                        clientSocket.close();
                        in.close();
                        break;
                    }
                }
                //whether to close after a single request.
                // the interrupt is critical otherwise cannot easily shutdown
            }

        } catch (IOException e) {
            System.out.println("Exception in echo server. "
                    + "\nExpected when shutdown. {}" + e.getLocalizedMessage());
        } finally {
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();

        }
    }

    private ServerSocket getServer() {
        return serverSocket;
    }

    private void setServer(ServerSocket server) {
        this.serverSocket = server;
    }
}