package org.atpprojectpartc;

import algorithms.search.AState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * MazeDisplayer is a JavaFX Canvas component designed for rendering and displaying maze games.
 * It extends the JavaFX Canvas class and provides functionality for drawing mazes, managing player position,
 * displaying solution paths, and handling custom images for different maze elements.
 *
 * <p>The class uses a fixed cell size architecture where each maze cell is rendered as a 55x55 pixel square.
 * The canvas automatically resizes based on maze dimensions to accommodate different maze sizes.</p>
 *
 * <p>Key features include:</p>
 * <ul>
 *   <li>Fixed cell size rendering (55 pixels per cell)</li>
 *   <li>Custom image support for walls, player, goal, and solution path</li>
 *   <li>Automatic fallback to colored rectangles when images are unavailable</li>
 *   <li>Solution path overlay functionality</li>
 *   <li>Dynamic canvas resizing based on maze dimensions</li>
 * </ul>
 */
public class MazeDisplayer extends Canvas {

    /** 2D array representing the maze structure where 1 = wall, 0 = path */
    private int[][] maze;

    /** Flag indicating whether to display the solution path overlay */
    private boolean solutionMode = false;

    /** Array storing the goal position as [row, col] */
    private int[] goalPos = new int[2];

    /** Current row position of the player */
    private int playerRow;

    /** Current column position of the player */
    private int playerCol;

    /** Collection of AState objects representing the solution path */
    private ArrayList<AState> solutionPath;

    /** StringProperty for the wall image file path */
    StringProperty imageFileNameWall = new SimpleStringProperty();

    /** StringProperty for the player image file path */
    StringProperty imageFileNamePlayer = new SimpleStringProperty();

    /** StringProperty for the goal image file path */
    StringProperty imageFileNameGoal = new SimpleStringProperty();

    /** StringProperty for the solution path image file path */
    StringProperty imageFileNameSol = new SimpleStringProperty();

    /** Fixed cell size in pixels - remains constant regardless of maze size */
    private static final double CELL_SIZE = 55.0;

    /**
     * Default constructor that creates a MazeDisplayer with default canvas size.
     * The canvas size will be automatically updated when a maze is set.
     */
    public MazeDisplayer() {
        super(600, 400); // Default size, will be updated when maze is set
    }

    /**
     * Constructor that creates a MazeDisplayer with specified initial dimensions.
     * The canvas size will be automatically updated when a maze is set.
     *
     * @param width Initial canvas width
     * @param height Initial canvas height
     */
    public MazeDisplayer(double width, double height) {
        super(width, height); // Initial size, will be updated when maze is set
    }

    /**
     * Gets the current row position of the player.
     *
     * @return The player's current row position
     */
    public int getPlayerRow() {
        return playerRow;
    }

    /**
     * Gets the current column position of the player.
     *
     * @return The player's current column position
     */
    public int getPlayerCol() {
        return playerCol;
    }

    /**
     * Sets the player's position and triggers a redraw of the canvas.
     *
     * @param row The new row position for the player
     * @param col The new column position for the player
     */
    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    /**
     * Sets the goal position in the maze.
     *
     * @param rowGoal The row position of the goal
     * @param colGoal The column position of the goal
     */
    public void setGoal(int rowGoal, int colGoal) {
        this.goalPos = new int[] {rowGoal, colGoal};
    }

    /**
     * Gets the wall image file name.
     *
     * @return The current wall image file path
     */
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    /**
     * Gets the wall image file name property for binding.
     *
     * @return The StringProperty for the wall image file name
     */
    public StringProperty imageFileNameWallProperty() {
        return imageFileNameWall;
    }

    /**
     * Sets the wall image file name.
     *
     * @param imageFileNameWall The path to the wall image file
     */
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    /**
     * Gets the player image file name.
     *
     * @return The current player image file path
     */
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    /**
     * Gets the player image file name property for binding.
     *
     * @return The StringProperty for the player image file name
     */
    public StringProperty imageFileNamePlayerProperty() {
        return imageFileNamePlayer;
    }

    /**
     * Sets the player image file name.
     *
     * @param imageFileNamePlayer The path to the player image file
     */
    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    /**
     * Gets the goal image file name property for binding.
     *
     * @return The StringProperty for the goal image file name
     */
    public StringProperty imageFileNamePlayerGoalProperty() {
        return imageFileNameGoal;
    }

    /**
     * Sets the goal image file name.
     *
     * @param imageFileNameGoal The path to the goal image file
     */
    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }

    /**
     * Gets the goal image file name.
     *
     * @return The current goal image file path
     */
    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    /**
     * Sets the solution path image file name.
     *
     * @param imageFileNameSol The path to the solution path image file
     */
    public void setImageFileNameSol(String imageFileNameSol) {
        this.imageFileNameSol.set(imageFileNameSol);
    }

    /**
     * Gets the solution path image file name.
     *
     * @return The current solution path image file path
     */
    public String getImageFileNameSol() {
        return imageFileNameSol.get();
    }

    /**
     * Draws the maze on the canvas and automatically resizes the canvas based on maze dimensions.
     * The canvas size is calculated using the formula: width = cols * CELL_SIZE, height = rows * CELL_SIZE.
     *
     * @param maze 2D integer array representing the maze where 1 = wall, 0 = path
     */
    public void drawMaze(int[][] maze) {
        this.maze = maze;

        // Update canvas size based on maze dimensions and fixed cell size
        if (maze != null) {
            int rows = maze.length;
            int cols = maze[0].length;

            double newWidth = cols * CELL_SIZE;
            double newHeight = rows * CELL_SIZE;

            // Resize the canvas to fit the maze exactly
            setWidth(newWidth);
            setHeight(newHeight);
        }

        draw();
    }

    /**
     * Gets the fixed cell size used for rendering maze elements.
     * This is useful for external calculations and positioning.
     *
     * @return The fixed cell size in pixels (55.0)
     */
    public static double getCellSize() {
        return CELL_SIZE;
    }

    /**
     * Main drawing method that orchestrates the rendering of all maze elements.
     * The rendering order is:
     * 1. Clear canvas
     * 2. Draw maze walls
     * 3. Draw solution path (if solution mode is enabled)
     * 4. Draw player
     * 5. Draw goal
     */
    private void draw() {
        if(maze != null){
            int rows = maze.length;
            int cols = maze[0].length;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, getWidth(), getHeight());

            drawMazeWalls(graphicsContext, rows, cols);
            if (solutionMode){
                drawSolPath(graphicsContext);
            }
            drawPlayer(graphicsContext);
            drawGoal(graphicsContext);
        }
    }

    /**
     * Renders the maze walls using either custom images or red rectangles as fallback.
     * Iterates through the maze array and draws walls where maze[i][j] == 1.
     *
     * @param graphicsContext The GraphicsContext for drawing operations
     * @param rows Number of rows in the maze
     * @param cols Number of columns in the maze
     */
    private void drawMazeWalls(GraphicsContext graphicsContext, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(getClass().getResourceAsStream(getImageFileNameWall()));
        } catch (Exception e) {
            System.out.println("There is no wall image file: " + getImageFileNameWall());
            e.printStackTrace();
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze[i][j] == 1){
                    double x = j * CELL_SIZE;
                    double y = i * CELL_SIZE;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    else
                        graphicsContext.drawImage(wallImage, x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    /**
     * Sets whether to display the solution path overlay.
     *
     * @param mode true to show solution path, false to hide it
     */
    public void setSolutionMode(boolean mode){
        this.solutionMode = mode;
    }

    /**
     * Sets the solution path to be displayed when solution mode is enabled.
     *
     * @param sol ArrayList of AState objects representing the solution path
     */
    public void setSolutionPath(ArrayList<AState> sol){
        this.solutionPath = sol;
    }

    /**
     * Renders the solution path overlay by parsing AState string representations.
     * Each AState is expected to have a string format of "{row,col}".
     * Uses custom image if available, otherwise draws red rectangles.
     *
     * @param graphicsContext The GraphicsContext for drawing operations
     */
    private void drawSolPath(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.RED);

        Image pathImage = null;
        try{
            pathImage = new Image(getClass().getResourceAsStream(getImageFileNameSol()));
        } catch (Exception e) {
            System.out.println("There is no wall image file: " + getImageFileNameWall());
            e.printStackTrace();
        }
        double row, col, x, y;
        int startIndex, endIndex;
        String pos;
        for (int i = 0; i < solutionPath.size(); i++) {
            pos = solutionPath.get(i).toString();

            startIndex = pos.indexOf('{') + 1;
            endIndex = pos.indexOf(',');
            String rowValue = pos.substring(startIndex, endIndex);
            row = Integer.parseInt(rowValue);

            startIndex = pos.indexOf(',') + 1;
            endIndex = pos.indexOf('}');
            String colValue = pos.substring(startIndex, endIndex);
            col = Integer.parseInt(colValue);

            x = col * CELL_SIZE;
            y = row * CELL_SIZE;
            if (pathImage == null)
                graphicsContext.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            else{
                graphicsContext.drawImage(pathImage, x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    /**
     * Renders the player at the current player position.
     * Uses custom player image if available, otherwise draws a green rectangle.
     *
     * @param graphicsContext The GraphicsContext for drawing operations
     */
    private void drawPlayer(GraphicsContext graphicsContext) {
        double x = getPlayerCol() * CELL_SIZE;
        double y = getPlayerRow() * CELL_SIZE;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(getClass().getResourceAsStream(getImageFileNamePlayer()));
        } catch (Exception e) {
            System.out.println("There is no player image file: " + getImageFileNamePlayer());
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        else
            graphicsContext.drawImage(playerImage, x, y, CELL_SIZE, CELL_SIZE);
    }

    /**
     * Renders the goal at the specified goal position.
     * Uses custom goal image if available, otherwise draws a green rectangle.
     *
     * @param graphicsContext The GraphicsContext for drawing operations
     */
    private void drawGoal(GraphicsContext graphicsContext) {
        // Implementation for start and goal positions if needed

        double x = goalPos[1] * CELL_SIZE;
        double y = goalPos[0] * CELL_SIZE;
        graphicsContext.setFill(Color.GREEN);

        Image goalImage = null;
        try {
            goalImage = new Image(getClass().getResourceAsStream(getImageFileNameGoal()));
        } catch (Exception e) {
            System.out.println("There is no player image file: " + getImageFileNameGoal());
        }
        if(goalImage == null)
            graphicsContext.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        else
            graphicsContext.drawImage(goalImage, x, y, CELL_SIZE, CELL_SIZE);
    }
}