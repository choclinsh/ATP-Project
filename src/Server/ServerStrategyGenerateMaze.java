package Server;
import java.io.*;
import java.lang.reflect.Constructor;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;

/**
 * Server strategy implementation for handling maze generation requests with configurable algorithms.
 *
 * This strategy now uses the configured maze generation algorithm instead of being hardcoded.
 * The algorithm is specified in the configuration file and loaded dynamically using reflection.
 */
public class ServerStrategyGenerateMaze implements IServerStrategy{
    /** Counter for tracking and logging individual requests */
    private static int requestCounter = 0;

    /** Configuration manager instance */
    private Configurations config;

    /**
     * Creates a new maze generation strategy that uses configured algorithms.
     */
    public ServerStrategyGenerateMaze() {
        this.config = Configurations.getInstance();
    }

    /**
     * Handles a maze generation request using the configured algorithm.
     *
     * The maze generation algorithm is determined by the configuration file,
     * allowing changes without code modification.
     *
     * @param inFromClient Input stream to read client requests from
     * @param outToClient Output stream to send responses to the client
     */
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        int currentRequest = ++requestCounter;
        System.out.println("\nRequest " + currentRequest + " started");

        try {
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(byteArrayOutputStream);

            // Read maze dimensions from client
            int[] sizeArr = (int[]) fromClient.readObject();
            String algorithmName = config.getMazeGeneratingAlgorithm();

            System.out.println("Request " + currentRequest + ": Generating maze of size " +
                    sizeArr[0] + "x" + sizeArr[1] + " using " + algorithmName);

            // Create maze generator using configured algorithm
            AMazeGenerator mazeGenerator = createMazeGenerator(algorithmName);
            Maze maze = mazeGenerator.generate(sizeArr[0], sizeArr[1]);

            // Compress and send maze
            compressor.write(maze.toByteArray());
            compressor.flush();
            byte[] compressedMaze = byteArrayOutputStream.toByteArray();

            toClient.writeObject(compressedMaze);
            toClient.flush();

            System.out.println("Request " + currentRequest + ": Maze generated and sent");

            fromClient.close();
            toClient.close();
        } catch (Exception e) {
            System.err.println("Request " + currentRequest + ": Error - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a maze generator instance using reflection based on the algorithm name.
     *
     * @param algorithmName The name of the algorithm class
     * @return An instance of the specified maze generator
     * @throws Exception If the algorithm class cannot be found or instantiated
     */
    private AMazeGenerator createMazeGenerator(String algorithmName) throws Exception {
        try {
            // Try to load the class from the algorithms.mazeGenerators package
            String fullClassName = "algorithms.mazeGenerators." + algorithmName;
            Class<?> algorithmClass = Class.forName(fullClassName);
            Constructor<?> constructor = algorithmClass.getConstructor();
            return (AMazeGenerator) constructor.newInstance();
        } catch (Exception e) {
            System.err.println("Failed to create maze generator '" + algorithmName + "': " + e.getMessage());
            System.err.println("Using default MyMazeGenerator");
            // Fallback to default
            return new algorithms.mazeGenerators.MyMazeGenerator();
        }
    }
}