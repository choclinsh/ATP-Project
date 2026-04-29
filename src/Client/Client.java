package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * TCP client for communicating with a server using a strategy pattern.
 * Establishes a socket connection to the server and delegates communication
 * logic to a pluggable client strategy implementation.
 *
 * This class handles the network connection setup and teardown, while
 * the actual communication protocol is defined by the IClientStrategy.
 */
public class Client {
    /** IP address of the server to connect to */
    private InetAddress serverIP;

    /** Port number of the server */
    private int serverPort;

    /** Strategy object that defines the communication protocol */
    private IClientStrategy strategy;

    /**
     * Creates a new client with the specified server connection parameters and communication strategy.
     *
     * @param serverIP The IP address of the server to connect to
     * @param serverPort The port number of the server (should be > 0 and <= 65535)
     * @param strategy The communication strategy to use (cannot be null)
     */
    public Client(InetAddress serverIP, int serverPort, IClientStrategy strategy) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.strategy = strategy;
    }

    /**
     * Establishes a connection to the server and executes the communication strategy.
     *
     * Creates a TCP socket connection to the specified server IP and port,
     * then delegates the actual communication to the strategy object.
     * The socket is automatically closed when communication completes or if an error occurs.
     *
     * This method uses try-with-resources to ensure proper socket cleanup.
     * Connection status is logged to the console for debugging purposes.
     *
     * @throws RuntimeException If the strategy is null (unchecked)
     */
    public void communicateWithServer(){
        try(Socket serverSocket = new Socket(serverIP, serverPort)){
            System.out.println("connected to server - IP = " + serverIP + ", Port = " + serverPort);

            // Delegate communication logic to the strategy
            strategy.clientStrategy(serverSocket.getInputStream(), serverSocket.getOutputStream());

        } catch (IOException e) {
            System.err.println("Failed to communicate with server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}