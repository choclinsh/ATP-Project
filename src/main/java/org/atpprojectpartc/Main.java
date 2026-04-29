package org.atpprojectpartc;

import Server.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.atpprojectpartc.View.SoundManager;
import org.atpprojectpartc.View.WelcomeWindow;

import java.util.Optional;
import java.util.Scanner;

public class Main extends Application {
    private static Server mazeGeneratingServer;
    private static Server mazeSolver;
    private SoundManager soundManager = new SoundManager();
    @Override
    public void start(Stage primaryStage) throws Exception{
        mazeGeneratingServer = new Server(5400, 5000, new ServerStrategyGenerateMaze());
        mazeSolver = new Server(5401, 5000, new ServerStrategySolveSearchProblem());
        //Starting server
        mazeGeneratingServer.start();
        mazeSolver.start();
        Parent root = FXMLLoader.load(getClass().getResource("/org/atpprojectpartc/View/MyView.fxml"));

        primaryStage.setTitle("Maze GUI");
        primaryStage.setScene(new Scene(root, 1600, 900));// Set up proper shutdown handling
        setupShutdownHandling(primaryStage);
        // Set up proper shutdown handling
        primaryStage.setOnCloseRequest(event -> {
            // Prevent immediate close
            event.consume();

            // Show confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("Exit Application?");
            alert.setContentText("This will safely shut down all servers.");
            alert.initOwner(primaryStage);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                safeExit(primaryStage); // Call your safe exit method
            }
            // If user clicks Cancel or closes dialog, window stays open
        });
        WelcomeWindow.showWelcome(primaryStage);
        soundManager.playSound("/org/atpprojectpartc/View/Sounds/welcome.mp3", false);
        primaryStage.show();

    }


    /**
     * Set up proper shutdown handling for when user closes the window
     */
    private void setupShutdownHandling(Stage primaryStage) {
        // Handle window close request
        primaryStage.setOnCloseRequest(event -> {
            event.consume(); // Prevent immediate close
            safeExit(primaryStage);
        });

        // Handle application shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook triggered - cleaning up resources...");
            stopServers();
        }));
    }

    /**
     * Safely exit the application with proper cleanup
     * This method can be called from your controller
     */
    public static void safeExit(Stage primaryStage) {
        System.out.println("Initiating safe application shutdown...");

        try {
            // Step 1: Stop servers
            stopServers();

            // Step 4: Exit JavaFX application
            Platform.runLater(() -> {
                if (primaryStage != null) {
                    primaryStage.close();
                }
                Platform.exit();
            });

            // Step 5: Force system exit if needed (after a short delay)
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Give time for graceful shutdown
                    System.out.println("Application shutdown complete.");
                    System.exit(0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.exit(1);
                }
            }).start();

        } catch (Exception e) {
            System.err.println("Error during shutdown: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Stop the maze servers safely
     */
    private static void stopServers() {
        System.out.println("Stopping servers...");

        if (mazeGeneratingServer != null) {
            try {
                mazeGeneratingServer.stop();
                System.out.println("Maze generating server stopped");
            } catch (Exception e) {
                System.err.println("Error stopping maze generating server: " + e.getMessage());
            }
        }

        if (mazeSolver != null) {
            try {
                mazeSolver.stop();
                System.out.println("Maze solver server stopped");
            } catch (Exception e) {
                System.err.println("Error stopping maze solver server: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
