package org.atpprojectpartc.Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.stage.Stage;
import org.atpprojectpartc.View.Direction;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface defining the contract for Model components in the MVVM pattern.
 * This interface encapsulates all business logic and data operations for the maze game,
 * including maze generation, player movement, persistence, and solving algorithms.
 *
 * The Model is responsible for:
 * - Managing maze data and game state
 * - Communicating with external services (maze generation and solving servers)
 * - Handling player position and movement validation
 * - Providing persistence functionality (save/load)
 * - Enforcing game rules and logic
 */
public interface IModel {

    /**
     * Generates a new maze with the specified dimensions by communicating with a maze generation server.
     * This method establishes a connection to the maze generation service, sends the dimensions,
     * receives a compressed maze, decompresses it, and initializes the player position.
     *
     * @param rows String representation of the number of rows for the maze (must be parseable as integer)
     * @param cols String representation of the number of columns for the maze (must be parseable as integer)
     * @return Maze object representing the generated maze with start and goal positions
     * @throws IOException if there's a network communication error or I/O problem during maze generation
     * @throws ClassNotFoundException if there's a deserialization problem with the received maze data
     * @throws NumberFormatException if rows or cols cannot be parsed as integers
     * @throws RuntimeException if maze generation is interrupted or encounters unexpected errors
     */
    public Maze generateMaze(String rows, String cols) throws IOException, ClassNotFoundException;

    /**
     * Retrieves the current position of the player in the maze.
     * The position is represented as a two-element array where:
     * - Index 0 contains the row coordinate
     * - Index 1 contains the column coordinate
     *
     * @return int array of length 2 containing [row, column] coordinates of the player's current position
     *         Returns null if the player position has not been initialized
     */
    public int[] getPlayerLocation();

    /**
     * Attempts to move the player in the specified direction and validates the move.
     * This method handles all movement logic including:
     * - Calculating the new position based on the direction
     * - Validating that the target position is within maze bounds
     * - Checking that the target cell is traversable (not a wall)
     * - For diagonal moves, ensuring at least one adjacent path is clear
     * - Detecting if the player has reached the goal position
     *
     * Movement directions supported:
     * - Cardinal: UP, DOWN, LEFT, RIGHT
     * - Diagonal: UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
     *
     * @param direction Direction enum value indicating the desired movement direction
     * @return Move enum indicating the result:
     *         - Move.VALID: Move was successful and player position updated
     *         - Move.INVALID: Move was blocked (wall, boundary, or invalid diagonal path)
     *         - Move.GOAL: Move was successful and player reached the goal position
     */
    public Move setPlayerLocation(Direction direction);

    /**
     * Opens a file chooser dialog to allow the user to save the current maze to a file.
     * This method handles the complete save workflow:
     * - Validates that a maze exists to save
     * - Presents a file chooser dialog with appropriate filters (.maze files)
     * - Sets the initial directory to the project's resources folder
     * - Serializes the maze object to the selected file
     * - Shows success/error alerts to inform the user of the operation result
     *
     * @param stage The parent Stage for centering the file chooser dialog and alert messages
     *              If null, dialogs will not be properly centered
     */
    public void saveMazeWithFileChooser(Stage stage);

    /**
     * Opens a file chooser dialog to allow the user to load a maze from a file.
     * This method handles the complete load workflow:
     * - Presents a file chooser dialog with appropriate filters (.maze files)
     * - Sets the initial directory to the project's resources folder
     * - Deserializes the maze object from the selected file
     * - Updates the current maze and resets player position to the maze's start position
     * - Shows success/error alerts to inform the user of the operation result
     *
     * @param stage The parent Stage for centering the file chooser dialog and alert messages
     *              If null, dialogs will not be properly centered
     * @return Maze object loaded from the file, or null if:
     *         - User cancelled the file selection
     *         - File loading failed due to I/O errors
     *         - File deserialization failed (corrupted or incompatible format)
     */
    public Maze loadMazeWithFileChooser(Stage stage);

    /**
     * Solves the current maze using an external solving algorithm service.
     * This method communicates with a maze solving server to obtain the optimal solution path
     * from the current maze's start position to the goal position.
     *
     * The solving process:
     * - Sends the current maze object to the solving service
     * - Receives a Solution object containing the optimal path
     * - Extracts the solution path as a list of AState objects
     *
     * @return ArrayList of AState objects representing the step-by-step solution path
     *         from start to goal position. Each AState contains position coordinates
     *         and parent state information for path reconstruction.
     * @throws IOException if there's a network communication error with the solving service
     * @throws ClassNotFoundException if there's a deserialization problem with the solution data
     * @throws RuntimeException if solving is interrupted or encounters unexpected errors
     * @throws IllegalStateException if no maze is currently loaded (maze is null)
     */
    public ArrayList<AState> solveMaze() throws IOException, ClassNotFoundException;

    /**
     * Retrieves the current maze's cell data as a 2D integer array.
     * This method provides access to the raw maze structure where:
     * - 0 represents a path cell (traversable)
     * - 1 represents a wall cell (non-traversable)
     *
     * The returned array follows the convention:
     * - First dimension: rows (maze.getRows())
     * - Second dimension: columns (maze.getCols())
     * - array[row][col] gives the cell value at position (row, col)
     *
     * @return 2D int array representing the maze structure, or null if no maze is currently loaded
     *         The array is a direct reference to the maze's internal structure
     */
    public int[][] getCurrMaze();
}