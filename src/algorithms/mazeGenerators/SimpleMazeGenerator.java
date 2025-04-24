package algorithms.mazeGenerators;
import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator{
    public SimpleMazeGenerator(){
        super();
    }

    public Maze generate(int rows, int cols){
        if (rows < 2 || cols < 2)
        {
            return null;
        }
        Maze new_maze = new Maze(rows, cols);
        int[][] maze = new_maze.getMaze();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1){
                    maze[i][j] = 0;
                }
                else {
                    maze[i][j] = 1;
                }
            }
        }
        Position start = new_maze.getStartPosition();  // decide the start and goal position. next times that this func is called, return
        // a deep copy of position
        Position goal = new_maze.getGoalPosition();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1){
                    if ((i != start.getRowIndex() && j != start.getColumnIndex()) || (i != goal.getRowIndex() && j != goal.getColumnIndex())) {
                        maze[i][j] = 1;
                    }
                }
            }
        }

        createPath(maze, start, goal, rows, cols);
        addRandomCells(maze, rows, cols);
        return new_maze;
    }
    private void createPath(int[][] maze, Position start, Position goal, int rows, int cols) {
        int currentRow = start.getRowIndex();
        int currentCol = start.getColumnIndex();
        int goalRow = goal.getRowIndex();
        int goalCol = goal.getColumnIndex();
        Random rd = new Random();

        // Create a path by moving toward the goal
        while (currentRow != goalRow || currentCol != goalCol) {
            int nextRow = currentRow;
            int nextCol = currentCol;

            // Decide whether to move horizontally or vertically
            if (currentRow != goalRow && currentCol != goalCol) {
                // If we need to move in both directions, randomly choose one
                if (rd.nextBoolean()) {
                    // Move horizontally
                    nextCol += (currentCol < goalCol) ? 1 : -1;
                } else {
                    // Move vertically
                    nextRow += (currentRow < goalRow) ? 1 : -1;
                }
            } else if (currentRow != goalRow) {
                // Only need to move vertically
                nextRow += (currentRow < goalRow) ? 1 : -1;
            } else {
                // Only need to move horizontally
                nextCol += (currentCol < goalCol) ? 1 : -1;
            }
            // Check if the new position is valid
            if (isValidPosition(nextRow, nextCol, rows, cols)) {
                currentRow = nextRow;
                currentCol = nextCol;
                // Mark the current cell as a path (0)
                maze[currentRow][currentCol] = 0;
            }
        }
    }

    private void addRandomCells(int[][] maze, int rows, int cols) {
        Random rd = new Random();

        // Add random paths inside the maze (not on the frame)
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                // Skip if it's already part of our guaranteed path (0)
                if (maze[i][j] == 0) {
                    continue;
                }
                // 30% chance of becoming a path (0), 70% remain as walls (1)
                if (rd.nextDouble() < 0.3) {
                    maze[i][j] = 0;
                }
            }
        }
    }

    private boolean isValidPosition(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}

