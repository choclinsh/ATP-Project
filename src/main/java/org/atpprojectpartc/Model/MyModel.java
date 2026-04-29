package org.atpprojectpartc.Model;
import Client.*;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.atpprojectpartc.View.Direction;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.io.*;
import java.net.InetAddress;

public class MyModel implements IModel{
    public InputStream inFromServer;
    public OutputStream outToServer;
    public int[] playerIndex;
    public Maze maze;
    public Solution solution;

    public Maze generateMaze(String rows, String cols) throws IOException, ClassNotFoundException {
        int row = Integer.valueOf(rows);
        int col = Integer.valueOf(cols);

        CompletableFuture<Maze> mazeFuture = new CompletableFuture<>();

        Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
            @Override
            public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();

                    int[] mazeDimensions = new int[]{row, col};
                    toServer.writeObject(mazeDimensions);
                    toServer.flush();

                    byte[] compressedMaze = (byte[]) fromServer.readObject();
                    InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));

                    byte[] decompressedMaze = new byte[row * col + 24];
                    int totalBytesRead = is.read(decompressedMaze);

                    if (totalBytesRead < decompressedMaze.length) {
                        byte[] trimmed = new byte[totalBytesRead];
                        System.arraycopy(decompressedMaze, 0, trimmed, 0, totalBytesRead);
                        decompressedMaze = trimmed;
                    }

                    Maze maze = new Maze(decompressedMaze);
                    mazeFuture.complete(maze); // Complete the future with the maze

                } catch (Exception e) {
                    mazeFuture.completeExceptionally(e); // Complete exceptionally if error occurs
                }
            }
        });

        client.communicateWithServer();

        try {
            maze = mazeFuture.get();
            playerIndex = new int[] {maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex()};
            return maze; // Block until the maze is available
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            } else if (cause instanceof ClassNotFoundException) {
                throw (ClassNotFoundException) cause;
            } else {
                throw new RuntimeException("Unexpected error during maze generation", cause);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Maze generation was interrupted", e);
        }
    }

    public ArrayList<AState> solveMaze() throws IOException, ClassNotFoundException {
        CompletableFuture<Solution> solFuture = new CompletableFuture<>();
        Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
            @Override
            public void clientStrategy(InputStream inFromServer,
                                       OutputStream outToServer) {
                try {
                    ObjectOutputStream toServer = new
                            ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new
                            ObjectInputStream(inFromServer);
                    toServer.writeObject(maze); //send maze to server
                    toServer.flush();
                    Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed withMyCompressor) from server
                    solFuture.complete(mazeSolution);
                } catch (Exception e) {
                    solFuture.completeExceptionally(e);
                }
            }
        });
        client.communicateWithServer();
        try {
            solution = solFuture.get();
            return solution.getSolutionPath();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            } else if (cause instanceof ClassNotFoundException) {
                throw (ClassNotFoundException) cause;
            } else {
                throw new RuntimeException("Unexpected error during maze generation", cause);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Maze generation was interrupted", e);
        }
    }

    public int[] getPlayerLocation(){
        return playerIndex;
    }

    public Move setPlayerLocation(Direction direction) {
        // Calculate new position based on direction
        int newRow = playerIndex[0];
        int newCol = playerIndex[1];

        switch (direction) {
            case UP:
                newRow = playerIndex[0] - 1;
                break;
            case DOWN:
                newRow = playerIndex[0] + 1;
                break;
            case LEFT:
                newCol = playerIndex[1] - 1;
                break;
            case RIGHT:
                newCol = playerIndex[1] + 1;
                break;
            case UP_LEFT:
                newRow = playerIndex[0] - 1;
                newCol = playerIndex[1] - 1;
                break;
            case UP_RIGHT:
                newRow = playerIndex[0] - 1;
                newCol = playerIndex[1] + 1;
                break;
            case DOWN_LEFT:
                newRow = playerIndex[0] + 1;
                newCol = playerIndex[1] - 1;
                break;
            case DOWN_RIGHT:
                newRow = playerIndex[0] + 1;
                newCol = playerIndex[1] + 1;
                break;
        }

        // Validate the move
        if (isValidMove(newRow, newCol, direction)) {
            // Update player position
            playerIndex[0] = newRow;
            playerIndex[1] = newCol;

            // Check if player reached the goal
            if (hasReachedGoal()) {
                return Move.GOAL;
            }
            return Move.VALID;
        } else {
            return Move.INVALID;
        }
    }

    private  boolean isValidMove(int row, int col, Direction direction) {
        // Check bounds for target position FIRST
        if (row < 0 || row >= maze.getRows() || col < 0 || col >= maze.getCols()) {
            return false;
        }

        // Check if target cell is traversable (0 = path, 1 = wall)
        if (maze.getCell(row, col) != 0) {
            return false;
        }

        // For diagonal moves, check that at least one adjacent side is traversable
        switch (direction) {
            case UP_LEFT:
                // Can only move diagonally up-left if either UP or LEFT is traversable
                boolean upTraversable = isPositionTraversable(playerIndex[0] - 1, playerIndex[1]);
                boolean leftTraversable = isPositionTraversable(playerIndex[0], playerIndex[1] - 1);
                return upTraversable || leftTraversable;

            case UP_RIGHT:
                // Can only move diagonally up-right if either UP or RIGHT is traversable
                boolean upTraversable2 = isPositionTraversable(playerIndex[0] - 1, playerIndex[1]);
                boolean rightTraversable = isPositionTraversable(playerIndex[0], playerIndex[1] + 1);
                return upTraversable2 || rightTraversable;

            case DOWN_LEFT:
                // Can only move diagonally down-left if either DOWN or LEFT is traversable
                boolean downTraversable = isPositionTraversable(playerIndex[0] + 1, playerIndex[1]);
                boolean leftTraversable2 = isPositionTraversable(playerIndex[0], playerIndex[1] - 1);
                return downTraversable || leftTraversable2;

            case DOWN_RIGHT:
                // Can only move diagonally down-right if either DOWN or RIGHT is traversable
                boolean downTraversable2 = isPositionTraversable(playerIndex[0] + 1, playerIndex[1]);
                boolean rightTraversable2 = isPositionTraversable(playerIndex[0], playerIndex[1] + 1);
                return downTraversable2 || rightTraversable2;

            // For normal directions, only basic checks are needed (already done above)
            case UP:
            case DOWN:
            case LEFT:
            case RIGHT:
            default:
                return true; // Target cell bounds and traversability already validated above
        }
    }

    private boolean isPositionTraversable(int row, int col) {
        // Check bounds first - CRITICAL to prevent ArrayIndexOutOfBoundsException
        if (row < 0 || row >= maze.getRows() || col < 0 || col >= maze.getCols()) {
            return false; // Out of bounds positions are not traversable
        }

        // Check if cell is traversable (0 = path, 1 = wall)
        return maze.getCell(row, col) == 0;
    }


    private boolean hasReachedGoal() {
        Position goalPos = maze.getGoalPosition();
        return playerIndex[0] == goalPos.getRowIndex() && playerIndex[1] == goalPos.getColumnIndex();
    }

    public void saveMazeWithFileChooser(Stage ownerStage) {
        // Get current maze from your model/data source

        if (maze == null) {
            showAlert(ownerStage, Alert.AlertType.WARNING, "No Maze",
                    "Please generate a maze first before saving!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        fileChooser.setInitialFileName("maze1.maze");

        // Set correct initial directory for your project structure
        File resourceDir = new File("src/main/resources/org/atpprojectpartc");
        if (!resourceDir.exists()) {
            resourceDir.mkdirs();
        }
        if (resourceDir.exists()) {
            fileChooser.setInitialDirectory(resourceDir);
        }

        // Add extension filter
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));

        File file = fileChooser.showSaveDialog(ownerStage); // Use the passed stage

        if (file != null) {
            try {
                saveMazeToFile(maze, file);
                showAlert(ownerStage, Alert.AlertType.INFORMATION, "Success",
                        "Maze saved successfully to:\n" + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert(ownerStage, Alert.AlertType.ERROR, "Error",
                        "Failed to save maze:\n" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void saveMazeToFile(Maze maze, File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {
            oos.writeObject(maze);
            oos.flush();
        }
    }

    public Maze loadMazeWithFileChooser(Stage ownerStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Maze");

        // Set correct initial directory
        File resourceDir = new File("src/main/resources/org/atpprojectpartc");
        if (resourceDir.exists()) {
            fileChooser.setInitialDirectory(resourceDir);
        }

        // Add extension filter
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));

        File file = fileChooser.showOpenDialog(ownerStage); // Use the passed stage

        if (file != null) {
            try {
                maze = loadMazeFromFile(file);
                playerIndex = new int[] {maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex()};
                showAlert(ownerStage, Alert.AlertType.INFORMATION, "Success",
                        "Maze loaded successfully from:\n" + file.getAbsolutePath());
                return maze;
            } catch (IOException | ClassNotFoundException e) {
                showAlert(ownerStage, Alert.AlertType.ERROR, "Error",
                        "Failed to load maze:\n" + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Maze loadMazeFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            return (Maze) ois.readObject();
        }
    }

    private static void showAlert(Stage owner, Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public int[][] getCurrMaze(){
        return maze.getMaze();
    }

}
