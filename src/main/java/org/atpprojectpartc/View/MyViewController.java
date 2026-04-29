package org.atpprojectpartc.View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.atpprojectpartc.Main;
import org.atpprojectpartc.MazeDisplayer;
import org.atpprojectpartc.Model.Move;
import org.atpprojectpartc.Model.PropertiesManager;
import org.atpprojectpartc.ViewModel.MyViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements Initializable, IView{
    private double currentZoom = 1.0;
    private static final double MIN_ZOOM = 0.5;
    private static final double MAX_ZOOM = 3.0;
    private static final double ZOOM_FACTOR = 0.1;
    private SoundManager soundManager = new SoundManager();
    private boolean victoryMode = false;
    private MyViewModel viewModel = new MyViewModel();
    @FXML
    public StackPane mazeContainer;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    private TextField textField_mazeRows;
    @FXML
    private TextField textField_mazeColumns;
    @FXML
    private VBox controlsVBox;
    @FXML
    private ComboBox<String> characterComboBox;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    StringProperty updateGoalRow = new SimpleStringProperty();
    StringProperty updateGoalCol = new SimpleStringProperty();

    @FXML
    public Label playerRow;
    @FXML
    public Label playerCol;
    @FXML
    public Label goalRow;
    @FXML
    public Label goalCol;


    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public String getUpdateGoalRow() {
        return updateGoalRow.get();
    }

    public void setUpdateGoalRow(int updateGoalRow) {
        this.updateGoalRow.set(updateGoalRow + "");
    }

    public String getUpdateGoalCol() {
        return updateGoalCol.get();
    }

    public void setUpdateGoalCol(int updateGoalCol) {
        this.updateGoalCol.set(updateGoalCol + "");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        goalRow.textProperty().bind(updateGoalRow);
        goalCol.textProperty().bind(updateGoalCol);

        // Setup ScrollPane and focus handling
        setupScrollPane();
        if (characterComboBox != null) {
            characterComboBox.setValue("Sonic"); // Ensure Sonic is selected by default
        }

        // Setup zoom handling after a short delay to ensure everything is initialized
        Platform.runLater(this::setupZoomHandling);
    }

    @FXML
    public void newMaze(ActionEvent actionEvent) {
        // Show the controls panel
        if (controlsVBox != null) {
            controlsVBox.setVisible(true);
            controlsVBox.setManaged(true);
        }

        // Clear any existing maze
        if (mazeDisplayer != null) {
            mazeDisplayer.getGraphicsContext2D().clearRect(0, 0, mazeDisplayer.getWidth(), mazeDisplayer.getHeight());
        }

        // Reset any victory state
        resetVictoryState();

        // Reset zoom
        resetZoom();

        // Reset text fields to default values
        if (textField_mazeRows != null) {
            textField_mazeRows.setText("50");
        }
        if (textField_mazeColumns != null) {
            textField_mazeColumns.setText("50");
        }

        // Clear position labels
        setUpdatePlayerRow(0);
        setUpdatePlayerCol(0);
        setUpdateGoalRow(0);
        setUpdateGoalCol(0);

        System.out.println("New maze mode activated - controls panel shown");
    }

    private void setupZoomHandling() {
        // Add event filter to the maze container instead of ScrollPane
        if (mazeContainer != null) {
            mazeContainer.addEventFilter(ScrollEvent.SCROLL, this::handleZoomScroll);
        }
    }

    private void setupScrollPane() {
        if (scrollPane != null) {
            // Ensure proper focus behavior for key events
            scrollPane.setFocusTraversable(true);
            mazeDisplayer.setFocusTraversable(true);
        }
    }

    /**
     * Show a simple properties summary
     */
    @FXML
    public void showPropertiesSummary(ActionEvent actionEvent) {
        Stage currentStage = (Stage) mazeDisplayer.getScene().getWindow();
        PropertiesManager.showPropertiesSummary(currentStage);
    }

    @FXML
    public void generateMaze(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (!validateMazeDimensions()) {
            return; // Stop if validation fails
        }
        resetVictoryState();
        Maze maze = viewModel.generateMaze(textField_mazeRows.getText(), textField_mazeColumns.getText());

        int goalRow = maze.getGoalPosition().getRowIndex();
        int goalCol = maze.getGoalPosition().getColumnIndex();
        mazeDisplayer.setGoal(goalRow, goalCol);
        setPlayerPosition(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
        setGoalPosition(goalRow, goalCol);
        mazeDisplayer.drawMaze(maze.getMaze());

        soundManager.playSound("/org/atpprojectpartc/View/Sounds/Jazz.mp3", true);

        Platform.runLater(this::scrollToPlayer);
    }

    private boolean validateMazeDimensions() {
        String rowsText = textField_mazeRows.getText().trim();
        String colsText = textField_mazeColumns.getText().trim();

        // Check if fields are empty
        if (rowsText.isEmpty() || colsText.isEmpty()) {
            showValidationError("Empty Fields",
                    "Please enter values for both rows and columns.");
            return false;
        }

        try {
            // Try to parse as integers
            int rows = Integer.parseInt(rowsText);
            int cols = Integer.parseInt(colsText);

            // Check if values are positive
            if (rows <= 1 || cols <= 1) {
                showValidationError("Invalid Values",
                        "Rows and columns must be positive numbers greater than 0.\n\n" +
                                "Current values: Rows = " + rows + ", Columns = " + cols);
                return false;
            }

            // Optional: Check for reasonable maximum values
            if (rows  > 185 || cols > 185 ) {
                showValidationError("Values Too Large",
                        "Maze dimensions are too large! Maximum allowed is 185x185. Largest size possible 175x175=" +
                                "30625.\n\n" +
                                "Current values: Rows = " + rows + ", Columns = " + cols + "\n" +
                                "Please enter smaller values for better performance.");
                return false;
            }

            // All validation passed
            return true;

        } catch (NumberFormatException e) {
            showValidationError("Invalid Input",
                    "Please enter valid integer numbers only.\n\n" +
                            "Invalid input:\n" +
                            "Rows: '" + rowsText + "'\n" +
                            "Columns: '" + colsText + "'\n\n" +
                            "Examples of valid input: 10, 25, 50");
            return false;
        }
    }

    private void showValidationError(String title, String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Input Validation Error");
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(message);

        // Set owner to center the dialog
        Stage stage = (Stage) mazeDisplayer.getScene().getWindow();
        if (stage != null) {
            errorAlert.initOwner(stage);
        }

        errorAlert.showAndWait();

        // Focus back to the text field for correction
        Platform.runLater(() -> textField_mazeRows.requestFocus());
    }

    public void solveMaze(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        ArrayList<AState> solPath = viewModel.solveMaze();
        mazeDisplayer.setSolutionMode(true);
        mazeDisplayer.setSolutionPath(solPath);
        mazeDisplayer.drawMaze(viewModel.getCurrMaze());
    }



    public void saveFile(ActionEvent actionEvent) {
        viewModel.saveMazeWithFileChooser((Stage) mazeDisplayer.getScene().getWindow());

    }

    public void loadFile(ActionEvent actionEvent) {
        Maze loadedMaze = viewModel.loadMazeWithFileChooser((Stage) mazeDisplayer.getScene().getWindow());

        if (loadedMaze != null) {
            if (controlsVBox != null) {
                controlsVBox.setVisible(true);
                controlsVBox.setManaged(true);
            }
            updateUIWithMaze(loadedMaze);
        }
    }

    private void updateUIWithMaze(Maze maze) {
        // Reset victory state
        resetVictoryState();
        int goalRow = maze.getGoalPosition().getRowIndex();
        int goalCol = maze.getGoalPosition().getColumnIndex();
        mazeDisplayer.setGoal(goalRow, goalCol);
        setPlayerPosition(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
        setGoalPosition(goalRow, goalCol);
        mazeDisplayer.drawMaze(maze.getMaze());

        soundManager.playSound("/org/atpprojectpartc/View/Sounds/Jazz.mp3", true);

        Platform.runLater(this::scrollToPlayer);
    }

    public void keyPressed(KeyEvent keyEvent) {
        // FIRST: Check if we're in victory mode
        if (victoryMode) {
            keyEvent.consume();
            return;
        }

        // SECOND: Check if a text field has focus (user is typing)
        if (keyEvent.getTarget() instanceof TextField) {
            // User is typing in a text field, don't process as game movement
            return; // Let the text field handle the key event
        }

        // THIRD: Check if maze exists and player position is initialized
        if (mazeDisplayer == null) {
            keyEvent.consume();
            return; // No maze loaded yet
        }

        // FOURTH: Additional safety check for ViewModel
        try {
            int[] currentPos = viewModel.getPlayerLocation();
            if (currentPos == null) {
                keyEvent.consume();
                return; // Player position not initialized
            }
        } catch (Exception e) {
            keyEvent.consume();
            return; // Model not ready
        }

        // NOW: Process the movement keys
        Direction direction = null;

        switch (keyEvent.getCode()) {
            case NUMPAD8, DIGIT8 -> direction = Direction.UP;
            case NUMPAD2, DIGIT2 -> direction = Direction.DOWN;
            case NUMPAD4, DIGIT4 -> direction = Direction.LEFT;
            case NUMPAD6, DIGIT6 -> direction = Direction.RIGHT;
            case NUMPAD7, DIGIT7 -> direction = Direction.UP_LEFT;
            case NUMPAD9, DIGIT9 -> direction = Direction.UP_RIGHT;
            case NUMPAD1, DIGIT1 -> direction = Direction.DOWN_LEFT;
            case NUMPAD3, DIGIT3 -> direction = Direction.DOWN_RIGHT;
            default -> {
                keyEvent.consume();
                return; // Invalid key, do nothing
            }
        }

        // Try to move the player
        try {
            Move move = viewModel.setPlayerLocation(direction);
            if (move == Move.INVALID) {
                keyEvent.consume();
            } else {
                int[] pos = viewModel.getPlayerLocation();
                setPlayerPosition(pos[0], pos[1]);
                // Auto-scroll to follow player movement
                scrollToPlayer();

                if (move == Move.GOAL) {
                    victoryMode = true; // Prevent further movement

                    // Create victory GIF
                    Image gifImage = new Image(getClass().getResource("/org/atpprojectpartc/View/images/dancebanana.gif").toExternalForm());
                    ImageView gifView = new ImageView(gifImage);
                    gifView.setPreserveRatio(true);
                    gifView.setFitWidth(300);

                    // Center the GIF in the StackPane
                    StackPane.setAlignment(gifView, javafx.geometry.Pos.CENTER);

                    // Add gif on top of the maze
                    mazeContainer.getChildren().add(gifView);
                    soundManager.playSound("/org/atpprojectpartc/View/Sounds/winsound.mp3", false);

                    PauseTransition hideMaze = new PauseTransition(Duration.seconds(7));
                    hideMaze.setOnFinished(e -> {
                        mazeDisplayer.setVisible(false); // Hide canvas instead of removing it
                    });
                    hideMaze.play();

                    // Second transition: Remove the GIF after 7 seconds total
                    PauseTransition removeGif = new PauseTransition(Duration.seconds(7));
                    removeGif.setOnFinished(e -> {
                        mazeContainer.getChildren().remove(gifView); // Remove the GIF
                        // Canvas stays hidden until new maze is generated
                    });
                    removeGif.play();
                }
                keyEvent.consume();
            }
        } catch (Exception e) {
            // Handle any unexpected errors gracefully
            System.err.println("Error processing player movement: " + e.getMessage());
            keyEvent.consume();
        }
    }



    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
        Platform.runLater(this::scrollToPlayer);
    }

    public void setSolutionMode(boolean mode){
        mazeDisplayer.setSolutionMode(mode);
    }

    public void setSolutionPath(ArrayList<AState> solPath){
        mazeDisplayer.setSolutionPath(solPath);
    }

    public void setGoalPosition(int row, int col){
        mazeDisplayer.setGoal(row, col);
        setUpdateGoalRow(row);
        setUpdateGoalCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    private void scrollToPlayer() {
        if (scrollPane != null && mazeDisplayer != null) {
            double cellSize = MazeDisplayer.getCellSize();

            // Calculate player position in pixels
            double playerX = mazeDisplayer.getPlayerCol() * cellSize;
            double playerY = mazeDisplayer.getPlayerRow() * cellSize;

            // Get viewport dimensions
            double viewportWidth = scrollPane.getViewportBounds().getWidth();
            double viewportHeight = scrollPane.getViewportBounds().getHeight();

            // Get content dimensions
            double contentWidth = mazeDisplayer.getWidth();
            double contentHeight = mazeDisplayer.getHeight();

            // Calculate scroll values to center the player
            if (contentWidth > viewportWidth) {
                double centerX = playerX + (cellSize / 2) - (viewportWidth / 2);
                double maxScrollX = contentWidth - viewportWidth;
                double hValue = Math.max(0, Math.min(1, centerX / maxScrollX));
                scrollPane.setHvalue(hValue);
            }

            if (contentHeight > viewportHeight) {
                double centerY = playerY + (cellSize / 2) - (viewportHeight / 2);
                double maxScrollY = contentHeight - viewportHeight;
                double vValue = Math.max(0, Math.min(1, centerY / maxScrollY));
                scrollPane.setVvalue(vValue);
            }
        }
    }

    public void resetZoom() {
        currentZoom = 1.0;
        if (mazeDisplayer != null && mazeContainer != null) {
            mazeDisplayer.setScaleX(1.0);
            mazeDisplayer.setScaleY(1.0);

            // Reset container size
            mazeContainer.setMinWidth(Region.USE_COMPUTED_SIZE);
            mazeContainer.setMinHeight(Region.USE_COMPUTED_SIZE);
            mazeContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);
            mazeContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        }
    }

    // Enhanced resetVictoryState method
    private void resetVictoryState() {
        victoryMode = false;
        setSolutionMode(false);
        // Remove any leftover GIFs
        mazeContainer.getChildren().removeIf(node -> node instanceof ImageView);

        // Make sure the maze displayer is visible and in the container
        if (!mazeContainer.getChildren().contains(mazeDisplayer)) {
            mazeContainer.getChildren().add(0, mazeDisplayer);
        }
        mazeDisplayer.setVisible(true);

        // Reset zoom when generating new maze
        resetZoom();

        // Re-setup zoom handling in case container was recreated
        setupZoomHandling();

        // Ensure focus is properly set
        mazeDisplayer.requestFocus();
    }

    private void handleZoomScroll(ScrollEvent scrollEvent) {
        // Only handle zoom if Ctrl is pressed
        if (scrollEvent.isControlDown()) {
            scrollEvent.consume(); // Prevent normal scrolling

            double deltaY = scrollEvent.getDeltaY();
            double oldZoom = currentZoom;

            // Determine zoom direction
            if (deltaY > 0) {
                // Scroll up = Zoom in
                currentZoom = Math.min(MAX_ZOOM, currentZoom + ZOOM_FACTOR);
            } else {
                // Scroll down = Zoom out
                currentZoom = Math.max(MIN_ZOOM, currentZoom - ZOOM_FACTOR);
            }

            // Only apply zoom if it actually changed
            if (oldZoom != currentZoom) {
                applyZoomToMaze();
            }
        }
        // If Ctrl is not pressed, let the scroll event pass through for normal scrolling
    }

    // Apply zoom transformation
    private void applyZoomToMaze() {
        if (mazeDisplayer != null && mazeContainer != null) {
            // Store current scroll position as ratios
            double hRatio = scrollPane.getHvalue();
            double vRatio = scrollPane.getVvalue();

            // Apply zoom to the maze displayer
            mazeDisplayer.setScaleX(currentZoom);
            mazeDisplayer.setScaleY(currentZoom);

            // Update the container size to match the scaled content
            double originalWidth = mazeDisplayer.getWidth();
            double originalHeight = mazeDisplayer.getHeight();

            // Set minimum size for the container to ensure scroll bars work
            mazeContainer.setMinWidth(originalWidth * currentZoom);
            mazeContainer.setMinHeight(originalHeight * currentZoom);
            mazeContainer.setPrefWidth(originalWidth * currentZoom);
            mazeContainer.setPrefHeight(originalHeight * currentZoom);

            // Use Platform.runLater to restore scroll position after layout update
            Platform.runLater(() -> {
                scrollPane.setHvalue(hRatio);
                scrollPane.setVvalue(vRatio);
            });

            System.out.println("Zoom level: " + String.format("%.1f", currentZoom * 100) + "%");
        }
    }

    @FXML
    public void showInstructions(ActionEvent actionEvent) {
        Alert instructionsAlert = new Alert(Alert.AlertType.INFORMATION);
        instructionsAlert.setTitle("Maze Game Instructions");
        instructionsAlert.setHeaderText("How to Play the Maze Game");

        // Set owner to center the dialog
        Stage stage = (Stage) mazeDisplayer.getScene().getWindow();
        if (stage != null) {
            instructionsAlert.initOwner(stage);
        }

        // Create detailed instructions content
        VBox content = createInstructionsContent();

        // Wrap content in ScrollPane to handle overflow
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefSize(700, 600);
        scrollPane.setMaxSize(700, 600);

        // Set the custom content
        instructionsAlert.getDialogPane().setContent(scrollPane);

        // Make the dialog resizable and set a LARGER size
        instructionsAlert.getDialogPane().setPrefSize(750, 650);  // INCREASED from 600x500
        instructionsAlert.getDialogPane().setMinSize(750, 650);
        instructionsAlert.setResizable(true);

        // Show the dialog
        instructionsAlert.showAndWait();
    }

    private VBox createInstructionsContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f8f9fa;");

        // Set content size constraints
        content.setPrefWidth(650);
        content.setMaxWidth(650);

        // Title
        Label titleLabel = new Label("🎮 Maze Game Instructions");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Objective section
        Label objectiveTitle = new Label("🎯 Objective:");
        objectiveTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        objectiveTitle.setStyle("-fx-text-fill: #e74c3c;");

        Label objectiveText = new Label(
                "Navigate from the Start position (S) to the Goal position (E) by moving through the maze paths."
        );
        objectiveText.setWrapText(true);
        objectiveText.setStyle("-fx-text-fill: #34495e;");
        objectiveText.setPrefWidth(600);
        objectiveText.setMaxWidth(600);



        // Game elements section
        Label elementsTitle = new Label("🧩 Game Elements:");
        elementsTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        elementsTitle.setStyle("-fx-text-fill: #e74c3c;");

        Label elementsText = new Label(
                "• S = Start Position - Where you begin\n" +
                        "• E = Goal Position (Goal sign) - Your destination \n" +
                        "• 0 = Path (White) - You can move through these\n" +
                        "• 1 = Wall (Bricks) - You cannot pass through walls\n" +
                        "•() = Solution (Red coins) - Lead you to the goal"
        );
        elementsText.setWrapText(true);
        elementsText.setStyle("-fx-text-fill: #34495e; -fx-font-family: monospace;");
        elementsText.setPrefWidth(600);
        elementsText.setMaxWidth(600);

        // Features section
        Label featuresTitle = new Label("✨ Features:");
        featuresTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        featuresTitle.setStyle("-fx-text-fill: #e74c3c;");

        Label featuresText = new Label(
                "• Auto-scroll: The view follows your movement\n" +
                        "• Zoom: Hold Ctrl and scroll mouse wheel to zoom in/out\n" +
                        "• Save/Load: Save your current maze and load past mazes\n" +
                        "• Generate: Create new random mazes\n" +
                        "• Solve: Get AI assistance to solve the maze"
        );
        featuresText.setWrapText(true);
        featuresText.setStyle("-fx-text-fill: #34495e;");
        featuresText.setPrefWidth(600);
        featuresText.setMaxWidth(600);

        // Tips section
        Label tipsTitle = new Label("💡 Tips:");
        tipsTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        tipsTitle.setStyle("-fx-text-fill: #e74c3c;");

        Label tipsText = new Label(
                "• Plan your route before moving\n" +
                        "• Use diagonal movement (1,3,7,9) for faster navigation\n" +
                        "• Invalid moves (into walls) are ignored\n" +
                        "• Watch the position indicators in the top panel\n" +
                        "• Victory celebration plays when you reach the goal! 🎉"
        );
        tipsText.setWrapText(true);
        tipsText.setStyle("-fx-text-fill: #34495e;");
        tipsText.setPrefWidth(600);
        tipsText.setMaxWidth(600);



        // Add all components
        content.getChildren().addAll(
                titleLabel,
                new Separator(),
                objectiveTitle, objectiveText,
                new Separator(),
                elementsTitle, elementsText,
                new Separator(),
                featuresTitle, featuresText,
                new Separator(),
                tipsTitle, tipsText
        );

        return content;
    }

    @FXML
    public void showControlsReference(ActionEvent actionEvent) {
        Alert controlsAlert = new Alert(Alert.AlertType.INFORMATION);
        controlsAlert.setTitle("Movement Controls");
        controlsAlert.setHeaderText("Keyboard Controls Reference");

        Stage stage = (Stage) mazeDisplayer.getScene().getWindow();
        if (stage != null) {
            controlsAlert.initOwner(stage);
        }

        // Create a visual representation of the numpad
        String controlsDisplay =
                "Movement Keys (NUMPAD or Numbers):\n\n" +
                        "┌─────┬─────┬─────┐\n" +
                        "│  7  │  8  │  9  │  ← Diagonal Up\n" +
                        "│ ↖   │  ↑  │  ↗  │     movements\n" +
                        "├─────┼─────┼─────┤\n" +
                        "│  4  │  5  │  6  │  ← Horizontal\n" +
                        "│  ←  │     │  →  │     movements\n" +
                        "├─────┼─────┼─────┤\n" +
                        "│  1  │  2  │  3  │  ← Diagonal Down\n" +
                        "│ ↙   │  ↓  │  ↘  │     movements\n" +
                        "└─────┴─────┴─────┘\n\n" +
                        "Note: Key 5 is not used for movement";

        TextArea textArea = new TextArea(controlsDisplay);
        textArea.setEditable(false);
        textArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        textArea.setPrefRowCount(15);
        textArea.setPrefColumnCount(35);

        controlsAlert.getDialogPane().setContent(textArea);
        controlsAlert.setResizable(true);
        controlsAlert.getDialogPane().setPrefSize(400, 400);
        controlsAlert.showAndWait();
    }

    /**
     * Show about dialog with game information
     */
    @FXML
    public void showAbout(ActionEvent actionEvent) {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About Maze Game");
        aboutAlert.setHeaderText("Maze Navigator v1.0");

        Stage stage = (Stage) mazeDisplayer.getScene().getWindow();
        if (stage != null) {
            aboutAlert.initOwner(stage);
        }

        String aboutText =
                "🎮 Maze Navigator Game\n\n" +
                        "A JavaFX-based maze solving game with:\n" +
                        "• Dynamic maze generation\n" +
                        "• Using server client\n" +
                        "• Using prim algorithm to create interesting patterns\n" +
                        "• Using BFS/DFS/BestFirstSearch for maze solver\n" +
                        "• 8-directional movement\n" +
                        "• Auto-solving capabilities\n" +
                        "• Save/Load functionality\n" +
                        "• Zoom and scroll features\n" +
                        "• Multiple character selection\n" +
                        "🎯 Challenge yourself to find the optimal path!\n" +
                        "💻 Built with JavaFX and advanced algorithms\n" +
                        "📅 2025 Edition. By Shoham Choclin and Shahar Shamir.";

        aboutAlert.setContentText(aboutText);
        aboutAlert.showAndWait();
    }

    @FXML
    public void exitApplication(ActionEvent actionEvent) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Exit Application");
        confirmAlert.setHeaderText("Are you sure you want to exit?");
        confirmAlert.setContentText("This will stop all servers and close the application safely.");

        // Set the owner to center the dialog
        confirmAlert.initOwner((Stage) mazeDisplayer.getScene().getWindow());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("User confirmed exit - initiating safe shutdown...");
            Main.safeExit( (Stage) mazeDisplayer.getScene().getWindow());
        } else {
            System.out.println("User cancelled exit");
        }
    }

    @FXML
    public void setCharacter(ActionEvent actionEvent) {
        String selectedCharacter = characterComboBox.getValue();
        if (selectedCharacter != null) {
            switch (selectedCharacter) {
                case "Sonic":
                    setSonic();
                    break;
                case "Knuckles":
                    setKnuckles();
                    break;
                case "Tails":
                    setTails();
                    break;
                default:
                    System.out.println("Unknown character: " + selectedCharacter);
            }
        }
    }

    // Individual character methods
    private void setSonic() {
        System.out.println("Sonic selected!");
        // Update the MazeDisplayer to use Sonic's image
        if (mazeDisplayer != null) {
            mazeDisplayer.setImageFileNamePlayer("/org/atpprojectpartc/View/images/sonic.png");
        }
    }

    private void setKnuckles() {
        System.out.println("Knuckles selected!");
        // Update the MazeDisplayer to use Knuckles' image
        if (mazeDisplayer != null) {
            mazeDisplayer.setImageFileNamePlayer("/org/atpprojectpartc/View/images/knuckles.png");
        }
    }

    private void setTails() {
        System.out.println("Tails selected!");
        // Update the MazeDisplayer to use Tails' image
        if (mazeDisplayer != null) {
            mazeDisplayer.setImageFileNamePlayer("/org/atpprojectpartc/View/images/tails.png");
        }
    }

    /**
     * Emergency exit without confirmation - for critical situations
     */
    @FXML
    public void forceExit(ActionEvent actionEvent) {
        System.out.println("Force exit triggered");
        Main.safeExit((Stage) mazeDisplayer.getScene().getWindow());
    }

}