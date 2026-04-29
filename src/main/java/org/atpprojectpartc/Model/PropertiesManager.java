package org.atpprojectpartc.Model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

/**
 * Manager class for handling configuration properties
 */
public class PropertiesManager {

    private static final String PROPERTIES_FILE_PATH = "/org/atpprojectpartc/config.properties";
    private static Properties properties;

    // Default properties values
    private static final String DEFAULT_THREAD_POOL_SIZE = "12";
    private static final String DEFAULT_MAZE_GENERATING_ALGORITHM = "MyMazeGenerator";
    private static final String DEFAULT_MAZE_SEARCHING_ALGORITHM = "BestFirstSearch";

    static {
        loadProperties();
    }

    /**
     * Load properties from the configuration file
     */
    private static void loadProperties() {
        properties = new Properties();

        try (InputStream inputStream = PropertiesManager.class.getResourceAsStream(PROPERTIES_FILE_PATH)) {
            if (inputStream != null) {
                properties.load(inputStream);
                System.out.println("Properties loaded successfully from: " + PROPERTIES_FILE_PATH);
            } else {
                System.err.println("Properties file not found: " + PROPERTIES_FILE_PATH);
                loadDefaultProperties();
            }
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
            loadDefaultProperties();
        }
    }

    /**
     * Load default properties if file is not found
     */
    private static void loadDefaultProperties() {
        properties.setProperty("threadPoolSize", DEFAULT_THREAD_POOL_SIZE);
        properties.setProperty("mazeGeneratingAlgorithm", DEFAULT_MAZE_GENERATING_ALGORITHM);
        properties.setProperty("mazeSearchingAlgorithm", DEFAULT_MAZE_SEARCHING_ALGORITHM);
        System.out.println("Default properties loaded");
    }

    /**
     * Get thread pool size from properties
     */
    public static int getThreadPoolSize() {
        try {
            return Integer.parseInt(properties.getProperty("threadPoolSize", DEFAULT_THREAD_POOL_SIZE));
        } catch (NumberFormatException e) {
            System.err.println("Invalid threadPoolSize value, using default: " + DEFAULT_THREAD_POOL_SIZE);
            return Integer.parseInt(DEFAULT_THREAD_POOL_SIZE);
        }
    }

    /**
     * Get maze generating algorithm from properties
     */
    public static String getMazeGeneratingAlgorithm() {
        return properties.getProperty("mazeGeneratingAlgorithm", DEFAULT_MAZE_GENERATING_ALGORITHM);
    }

    /**
     * Get maze searching algorithm from properties
     */
    public static String getMazeSearchingAlgorithm() {
        return properties.getProperty("mazeSearchingAlgorithm", DEFAULT_MAZE_SEARCHING_ALGORITHM);
    }

    /**
     * Show a simple properties summary dialog
     */
    public static void showPropertiesSummary(Stage owner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(owner);
        alert.setTitle("Configuration Summary");
        alert.setHeaderText("Current Configuration");

        String content = String.format(
                "Thread Pool Size: %d\n" +
                        "Maze Generator: %s\n" +
                        "Search Algorithm: %s",
                getThreadPoolSize(),
                getMazeGeneratingAlgorithm(),
                getMazeSearchingAlgorithm()
        );

        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Reload properties from file (useful for development)
     */
    public static void reloadProperties() {
        loadProperties();
        System.out.println("Properties reloaded");
    }

    /**
     * Get all properties as a formatted string
     */
    public static String getPropertiesAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Configuration Properties ===\n");

        for (String key : properties.stringPropertyNames()) {
            sb.append(key).append(" = ").append(properties.getProperty(key)).append("\n");
        }

        return sb.toString();
    }
}