package Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration manager for the maze server application.
 *
 * This class manages all configurable parameters for the application by reading
 * them from a properties file located in the resources directory. It ensures
 * that configuration changes don't require code modification or recompilation.
 *
 * The configuration file (properties.config) should be placed in the resources
 * directory alongside the src directory in the project structure.
 *
 * Supported configurations:
 * - threadPoolSize: Number of threads in the server's thread pool
 * - mazeGeneratingAlgorithm: Algorithm name for maze generation
 * - mazeSearchingAlgorithm: Algorithm name for maze solving
 */
public class Configurations {
    /** Singleton instance */
    private static Configurations singletonInstance = null;

    /** Properties object containing all configuration values */
    private Properties configValues;

    /** Name of the configuration file in resources directory */
    private static final String CONFIG_FILE = "config.properties";

    // Default configuration values
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;
    private static final String DEFAULT_MAZE_GENERATING_ALGORITHM = "MyMazeGenerator";
    private static final String DEFAULT_MAZE_SEARCHING_ALGORITHM = "BestFirstSearch";

    /**
     * Private constructor to prevent direct instantiation.
     * Loads configuration properties from the properties.config file.
     *
     * @throws RuntimeException If the configuration file cannot be loaded
     */
    private Configurations() {
        configValues = new Properties();
        loadProperties();
    }

    /**
     * Gets the singleton instance of the Configurations class.
     * Creates the instance if it doesn't exist (lazy initialization).
     *
     * @return The singleton Configurations instance
     */
    public static synchronized Configurations getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new Configurations();
        }
        return singletonInstance;
    }

    /**
     * Loads configuration properties from the properties.config file.
     * The file should be located in the resources directory.
     *
     * If the file cannot be loaded, default values will be used and
     * a warning message will be printed.
     */
    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                System.err.println("Warning: Configuration file '" + CONFIG_FILE + "' not found in resources directory.");
                System.err.println("Using default configuration values.");
                setDefaultProperties();
                return;
            }

            configValues.load(inputStream);
            System.out.println("Configuration loaded successfully from " + CONFIG_FILE);

            // Validate and log loaded configurations
            validateAndLogConfiguration();

        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            System.err.println("Using default configuration values.");
            setDefaultProperties();
        }
    }

    /**
     * Sets default configuration values when the config file cannot be loaded.
     */
    private void setDefaultProperties() {
        configValues.setProperty("threadPoolSize", String.valueOf(DEFAULT_THREAD_POOL_SIZE));
        configValues.setProperty("mazeGeneratingAlgorithm", DEFAULT_MAZE_GENERATING_ALGORITHM);
        configValues.setProperty("mazeSearchingAlgorithm", DEFAULT_MAZE_SEARCHING_ALGORITHM);
    }

    /**
     * Validates loaded configuration and logs the current settings.
     */
    private void validateAndLogConfiguration() {
        System.out.println("=== Current Configuration ===");
        System.out.println("Thread Pool Size: " + getThreadPoolSize());
        System.out.println("Maze Generating Algorithm: " + getMazeGeneratingAlgorithm());
        System.out.println("Maze Searching Algorithm: " + getMazeSearchingAlgorithm());
        System.out.println("=============================");
    }

    /**
     * Gets the configured thread pool size for the server.
     *
     * @return The number of threads to use in the thread pool (minimum 1)
     */
    public int getThreadPoolSize() {
        try {
            int size = Integer.parseInt(configValues.getProperty("threadPoolSize", String.valueOf(DEFAULT_THREAD_POOL_SIZE)));
            return Math.max(1, size); // Ensure at least 1 thread
        } catch (NumberFormatException e) {
            System.err.println("Invalid threadPoolSize value. Using default: " + DEFAULT_THREAD_POOL_SIZE);
            return DEFAULT_THREAD_POOL_SIZE;
        }
    }

    /**
     * Gets the configured maze generating algorithm name.
     *
     * @return The name of the algorithm to use for maze generation
     */
    public String getMazeGeneratingAlgorithm() {
        return configValues.getProperty("mazeGeneratingAlgorithm", DEFAULT_MAZE_GENERATING_ALGORITHM);
    }

    /**
     * Gets the configured maze searching algorithm name.
     *
     * @return The name of the algorithm to use for maze solving
     */
    public String getMazeSearchingAlgorithm() {
        return configValues.getProperty("mazeSearchingAlgorithm", DEFAULT_MAZE_SEARCHING_ALGORITHM);
    }
}
