package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server strategy implementation for solving maze search problems with configurable algorithms and caching.
 *
 * This strategy now uses the configured search algorithm instead of being hardcoded.
 * The algorithm is specified in the configuration file and loaded dynamically using reflection.
 */
public class ServerStrategySolveSearchProblem implements IServerStrategy{
    /** Thread-safe map storing associations between maze files and solution files */
    private final Map<Path, Path> mazeToSolMap = new ConcurrentHashMap<>();

    /** Counter for tracking and logging individual requests */
    private static int requestCounter = 0;

    /** Configuration manager instance */
    private Configurations config;

    /**
     * Creates a new maze solving strategy that uses configured algorithms.
     */
    public ServerStrategySolveSearchProblem() {
        this.config = Configurations.getInstance();
    }

    /**
     * Handles a maze solving request using the configured search algorithm.
     *
     * The search algorithm is determined by the configuration file,
     * allowing changes without code modification.
     *
     * @param inFromClient Input stream to read client requests from
     * @param outToClient Output stream to send responses to the client
     */
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        int currentRequest = ++requestCounter;
        System.out.println("\n=== Request " + currentRequest + " started ===");

        try {
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);

            // Read maze from client
            Maze maze = (Maze) fromClient.readObject();
            byte[] inputMazeBytes = maze.toByteArray();
            String algorithmName = config.getMazeSearchingAlgorithm();

            System.out.println("Request " + currentRequest + ": Received maze of size " +
                    maze.getRows() + "x" + maze.getCols() + " (will use " + algorithmName + ")");

            // Check cache for existing solution
            Path solPath = null;
            System.out.println("Request " + currentRequest + ": Checking cache (" +
                    mazeToSolMap.size() + " cached mazes)...");

            for (Path key : mazeToSolMap.keySet()) {
                byte[] data = Files.readAllBytes(key);
                if (Arrays.equals(data, inputMazeBytes)){
                    // Cache hit
                    solPath = mazeToSolMap.get(key);
                    System.out.println("Request " + currentRequest + ":  Using cached solution");
                    Solution solution = new Solution(Files.readAllBytes(solPath));
                    toClient.writeObject(solution);
                    toClient.flush();
                    System.out.println("Request " + currentRequest + ": Sent cached solution");
                    return;
                }
            }

            // Cache miss - solve using configured algorithm
            System.out.println("Request " + currentRequest + ": Cache miss - solving maze using " + algorithmName);
            long solveStartTime = System.currentTimeMillis();

            ISearchable searchableMaze = new SearchableMaze(maze);
            ISearchingAlgorithm searchAlgorithm = createSearchAlgorithm(algorithmName);
            Solution solution = searchAlgorithm.solve(searchableMaze);

            // Save to cache
            Path mazeFile = saveToTempFileMaze(inputMazeBytes);
            Path solFile = saveToTempFileSol(solution.toByteArray());
            mazeToSolMap.put(mazeFile, solFile);

            System.out.println("Request " + currentRequest + ": Saved to cache. Total cached: " +
                    mazeToSolMap.size());

            toClient.writeObject(solution);
            toClient.flush();

            System.out.println("Request " + currentRequest + ": Sent new solution");

        } catch (Exception e) {
            System.err.println("Request " + currentRequest + ": Error - " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("=== Request " + currentRequest + " completed ===\n");
        }
    }

    /**
     * Creates a search algorithm instance using reflection based on the algorithm name.
     *
     * @param algorithmName The name of the algorithm class
     * @return An instance of the specified search algorithm
     * @throws Exception If the algorithm class cannot be found or instantiated
     */
    private ISearchingAlgorithm createSearchAlgorithm(String algorithmName) throws Exception {
        try {
            // Try to load the class from the algorithms.search package
            String fullClassName = "algorithms.search." + algorithmName;
            Class<?> algorithmClass = Class.forName(fullClassName);
            Constructor<?> constructor = algorithmClass.getConstructor();
            return (ISearchingAlgorithm) constructor.newInstance();
        } catch (Exception e) {
            System.err.println("Failed to create search algorithm '" + algorithmName + "': " + e.getMessage());
            System.err.println("Using default BestFirstSearch");
            // Fallback to default
            return new BestFirstSearch();
        }
    }

    private static Path saveToTempFileMaze(byte[] data) throws IOException {
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        Path tempFile = Files.createTempFile(Paths.get(tempDirectoryPath), "mazeBytearray_", ".tmp");
        Files.write(tempFile, data);
        return tempFile;
    }

    private static Path saveToTempFileSol(byte[] data) throws IOException {
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        Path tempFile = Files.createTempFile(Paths.get(tempDirectoryPath), "solBytearray_", ".tmp");
        Files.write(tempFile, data);
        return tempFile;
    }
}