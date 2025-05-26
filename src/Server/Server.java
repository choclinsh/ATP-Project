package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multi-threaded TCP server that handles client connections using a configurable thread pool.
 *
 * This server uses the Strategy pattern to handle different types of client requests.
 * The thread pool size is now configurable through the Configurations singleton,
 * allowing changes without code modification or recompilation.
 *
 * @author [Your Name]
 * @version 2.0
 */
public class Server {
    /** Port number the server listens on */
    private int port;

    /** Timeout interval for socket operations in milliseconds */
    private int listeningIntervalMS;

    /** Strategy object that defines how to handle client requests */
    private IServerStrategy strategy;

    /** Flag to control server shutdown */
    private volatile boolean stop;

    /** Thread pool for handling client connections concurrently */
    private ExecutorService threadPool;

    /** Configuration manager instance */
    private Configurations config;

    /**
     * Creates a new server with the specified configuration.
     * The thread pool size is read from the configuration file.
     *
     * @param port The port number to listen on (should be > 0 and <= 65535)
     * @param listeningIntervalMS The socket timeout interval in milliseconds
     * @param strategy The strategy to use for handling client requests (cannot be null)
     */
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
        this.config = Configurations.getInstance();

        // Initialize thread pool with configurable size
        int threadPoolSize = config.getThreadPoolSize();
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        System.out.println("Server initialized with thread pool size: " + threadPoolSize);
    }

    /**
     * Starts the server and begins accepting client connections.
     * The behavior is the same as before, but now uses the configurable thread pool.
     */
    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            System.out.println("Starting server at port = " + port);

            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client accepted: " + clientSocket.toString());

                    // Submit client handling task to the configurable thread pool
                    threadPool.submit(() -> {
                        handleClient(clientSocket);
                    });

                } catch (SocketTimeoutException e){
                    System.out.println("Socket timeout");
                }
            }

            serverSocket.close();
            threadPool.shutdownNow();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    /**
     * Handles a single client connection using the configured strategy.
     * Implementation remains the same as the original version.
     *
     * @param clientSocket The socket connection to the client
     */
    private void handleClient(Socket clientSocket) {
        try {
            strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            System.out.println("Done handling client: " + clientSocket.toString());
            clientSocket.close();
        } catch (IOException e){
            System.out.println("IOException while handling client: " + e.getMessage());
        }
    }

    /**
     * Initiates server shutdown.
     */
    public void stop(){
        System.out.println("Stopping server...");
        stop = true;
    }
}