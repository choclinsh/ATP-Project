package org.atpprojectpartc.View;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Welcome window that appears when the maze game starts.
 * Provides an attractive introduction screen with instructions to begin the game.
 */
public class WelcomeWindow {

    private Stage welcomeStage;
    private Stage mainStage;

    /**
     * Creates and displays the welcome window
     * @param mainStage The main application stage that will be shown after welcome
     */
    public WelcomeWindow(Stage mainStage) {
        this.mainStage = mainStage;
        createWelcomeWindow();
    }

    private void createWelcomeWindow() {
        welcomeStage = new Stage();
        welcomeStage.initStyle(StageStyle.UNDECORATED);
        welcomeStage.setTitle("Welcome to Maze Game");

        // Create main container - WINDOW SIZE SETTING #1
        StackPane root = new StackPane();
        root.setMinSize(1600, 750);  // Changed from 800x500 to 1600x1000 (2x larger)

        // Create animated background
        Region background = createAnimatedBackground();

        // Create content container
        VBox contentBox = createContentBox();

        // Add components to root
        root.getChildren().addAll(background, contentBox);

        // Create scene - WINDOW SIZE SETTING #2
        Scene scene = new Scene(root, 1600, 750);  // Changed from 800x500 to 1600x1000 (2x larger)
        welcomeStage.setScene(scene);

        // Center the window
        welcomeStage.centerOnScreen();

        // Add fade-in animation
        addFadeInAnimation(root);

        // Show the welcome window
        welcomeStage.show();

        // Auto-close after 8 seconds or when clicked
        createAutoCloseTimer();
    }

    private Region createAnimatedBackground() {
        Region background = new Region();

        // Create gradient background using JavaFX CSS syntax
        background.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2, #f093fb);"
        );

        // Add subtle animation to background
        RotateTransition rotate = new RotateTransition(Duration.seconds(20), background);
        rotate.setByAngle(5);
        rotate.setAutoReverse(true);
        rotate.setCycleCount(Timeline.INDEFINITE);
        rotate.play();

        return background;
    }

    private VBox createContentBox() {
        VBox contentBox = new VBox(80);  // Doubled spacing: 40 -> 80
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(120));  // Doubled padding: 60 -> 120
        contentBox.setMaxWidth(1200);  // Doubled max width: 600 -> 1200

        // Create maze icon
        Label mazeIcon = createMazeIcon();

        // Create title
        Label titleLabel = createTitleLabel();

        // Create instruction
        Label instructionLabel = createInstructionLabel();

        // Create footer
        Label footerLabel = createFooterLabel();

        contentBox.getChildren().addAll(
                mazeIcon,
                titleLabel,
                instructionLabel,
                footerLabel
        );

        return contentBox;
    }

    private Label createMazeIcon() {
        Label mazeIcon = new Label("🎮");
        mazeIcon.setFont(Font.font("System", 120));  // Doubled font size: 60 -> 120

        // Add pulsing animation
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.5), mazeIcon);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.play();

        return mazeIcon;
    }

    private Label createTitleLabel() {
        Label titleLabel = new Label("Welcome to Maze Game");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 72));  // Doubled font size: 36 -> 72
        titleLabel.setTextFill(Color.WHITE);

        // Add drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setOffsetX(4);  // Doubled: 2 -> 4
        dropShadow.setOffsetY(4);  // Doubled: 2 -> 4
        dropShadow.setRadius(10);  // Doubled: 5 -> 10
        titleLabel.setEffect(dropShadow);

        return titleLabel;
    }

    private Label createSubtitleLabel() {
        Label subtitleLabel = new Label("Navigate through challenging mazes!");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 18));
        subtitleLabel.setTextFill(Color.WHITE);
        subtitleLabel.setStyle("-fx-font-style: italic;");

        // Add subtle drop shadow for better readability
        DropShadow textShadow = new DropShadow();
        textShadow.setColor(Color.BLACK);
        textShadow.setOffsetX(1);
        textShadow.setOffsetY(1);
        textShadow.setRadius(3);
        subtitleLabel.setEffect(textShadow);

        return subtitleLabel;
    }

    private Label createInstructionLabel() {
        Label instructionLabel = new Label("To start press File → New");
        instructionLabel.setFont(Font.font("System", FontWeight.BOLD, 44));  // Doubled font size: 22 -> 44
        instructionLabel.setTextFill(Color.WHITE);
        instructionLabel.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.3); " +
                        "-fx-background-radius: 30; " +  // Doubled radius: 15 -> 30
                        "-fx-padding: 40 70 40 70; " +   // Doubled padding: 20,35 -> 40,70
                        "-fx-border-color: rgba(255, 255, 255, 0.5); " +
                        "-fx-border-radius: 30; " +      // Doubled radius: 15 -> 30
                        "-fx-border-width: 4;"           // Doubled width: 2 -> 4
        );

        // Add glow effect
        DropShadow glow = new DropShadow();
        glow.setColor(Color.CYAN);
        glow.setRadius(40);  // Doubled radius: 20 -> 40
        glow.setSpread(0.3);
        instructionLabel.setEffect(glow);

        return instructionLabel;
    }

    private Button createStartButton() {
        Button startButton = new Button("Got it! Let's Start");
        startButton.setFont(Font.font("System", FontWeight.BOLD, 14));
        startButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #4CAF50, #45a049); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 25; " +
                        "-fx-padding: 12 30 12 30; " +
                        "-fx-border-color: #4CAF50; " +
                        "-fx-border-radius: 25; " +
                        "-fx-border-width: 2; " +
                        "-fx-cursor: hand;"
        );

        // Add hover effects
        startButton.setOnMouseEntered(e -> {
            startButton.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #5CBF60, #55b059); " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 25; " +
                            "-fx-padding: 12 30 12 30; " +
                            "-fx-border-color: #5CBF60; " +
                            "-fx-border-radius: 25; " +
                            "-fx-border-width: 2; " +
                            "-fx-cursor: hand; " +
                            "-fx-scale-x: 1.05; " +
                            "-fx-scale-y: 1.05;"
            );
        });

        startButton.setOnMouseExited(e -> {
            startButton.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #4CAF50, #45a049); " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 25; " +
                            "-fx-padding: 12 30 12 30; " +
                            "-fx-border-color: #4CAF50; " +
                            "-fx-border-radius: 25; " +
                            "-fx-border-width: 2; " +
                            "-fx-cursor: hand; " +
                            "-fx-scale-x: 1.0; " +
                            "-fx-scale-y: 1.0;"
            );
        });

        // Add click animation and close action
        startButton.setOnAction(e -> {
            ScaleTransition clickAnimation = new ScaleTransition(Duration.millis(100), startButton);
            clickAnimation.setFromX(1.05);
            clickAnimation.setFromY(1.05);
            clickAnimation.setToX(0.95);
            clickAnimation.setToY(0.95);
            clickAnimation.setOnFinished(event -> closeWelcomeWindow());
            clickAnimation.play();
        });

        return startButton;
    }

    private Label createFooterLabel() {
        Label footerLabel = new Label("Click anywhere or wait to continue...");
        footerLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        footerLabel.setTextFill(Color.WHITE);
        footerLabel.setStyle("-fx-font-style: italic;");

        // Add text shadow for better readability
        DropShadow textShadow = new DropShadow();
        textShadow.setColor(Color.BLACK);
        textShadow.setOffsetX(1);
        textShadow.setOffsetY(1);
        textShadow.setRadius(2);
        footerLabel.setEffect(textShadow);

        // Add blinking animation
        FadeTransition blink = new FadeTransition(Duration.seconds(1.5), footerLabel);
        blink.setFromValue(1.0);
        blink.setToValue(0.5);
        blink.setAutoReverse(true);
        blink.setCycleCount(Timeline.INDEFINITE);
        blink.play();

        return footerLabel;
    }

    private void addFadeInAnimation(StackPane root) {
        root.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private void createAutoCloseTimer() {
        // Close automatically after 8 seconds
        Timeline autoClose = new Timeline(
                new KeyFrame(Duration.seconds(8), e -> closeWelcomeWindow())
        );
        autoClose.play();

        // Also close when clicking anywhere on the window
        welcomeStage.getScene().setOnMouseClicked(e -> closeWelcomeWindow());

        // Close when pressing any key
        welcomeStage.getScene().setOnKeyPressed(e -> closeWelcomeWindow());
    }

    private void closeWelcomeWindow() {
        // Add fade-out animation before closing
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), welcomeStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            welcomeStage.close();
            // Show the main stage
            if (mainStage != null && !mainStage.isShowing()) {
                mainStage.show();
                mainStage.centerOnScreen();
            }
        });
        fadeOut.play();
    }

    /**
     * Shows the welcome window. This method should be called from your main application.
     * @param mainStage The main application stage to show after the welcome screen
     */
    public static void showWelcome(Stage mainStage) {
        Platform.runLater(() -> new WelcomeWindow(mainStage));
    }
}