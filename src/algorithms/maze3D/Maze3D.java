package algorithms.maze3D;

import java.util.Random;

/**
 * Represents a 3D maze with layers, rows, and columns.
 * The maze consists of a 3D array where 0 represents traversable paths and 1 represents walls.
 */
public class Maze3D {
    private int[][][] maze;
    private int depth;
    private int rows;
    private int cols;
    private Position3D start_pos;
    private Position3D goal_pos;

    /**
     * Constructs a 3D maze with the specified dimensions.
     *
     * @param depth The number of layers in the maze
     * @param rows The number of rows in each layer
     * @param cols The number of columns in each layer
     */
    public Maze3D(int depth, int rows, int cols) {
        this.maze = new int[depth][rows][cols];
        this.depth = depth;
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Gets the 3D array representing the maze structure.
     *
     * @return The 3D maze array
     */
    public int[][][] getMap() {
        return this.maze;
    }

    /**
     * Gets the value of a specific cell in the maze.
     *
     * @param depth The depth index
     * @param row The row index
     * @param col The column index
     * @return The cell value (0 for path, 1 for wall)
     */
    public int getCell(int depth, int row, int col) {
        return maze[depth][row][col];
    }

    /**
     * Gets the depth (number of layers) of the maze.
     *
     * @return The depth
     */
    public int getDepth() {
        return this.depth;
    }

    /**
     * Gets the number of rows in each layer of the maze.
     *
     * @return The number of rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Gets the number of columns in each layer of the maze.
     *
     * @return The number of columns
     */
    public int getCols() {
        return this.cols;
    }

    /**
     * Prints the 3D maze to the console, layer by layer.
     * Uses 'S' to mark the start position and 'E' to mark the end position.
     */
    public void print() {
        for (int d = 0; d < depth; d++) {
            System.out.println("Layer" + d + ":");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (i == start_pos.getRowIndex() && j == start_pos.getColumnIndex() && d == start_pos.getDepthIndex()) {
                        System.out.printf("%4c", 'S');
                    } else if (i == goal_pos.getRowIndex() && j == goal_pos.getColumnIndex() && d == goal_pos.getDepthIndex()) {
                        System.out.printf("%4c", 'E');
                    } else {
                        System.out.printf("%4d", maze[d][i][j]);
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /**
     * Gets the start position of the maze.
     * Initializes the start position if not already set.
     *
     * @return A copy of the start position
     */
    public Position3D getStartPosition() {
        if (start_pos == null)
        {
            start_pos = getPosition();
            return new Position3D(start_pos);
        }
        else {
            return new Position3D(start_pos);
        }
    }

    /**
     * Gets the goal position of the maze.
     * Initializes the goal position if not already set.
     *
     * @return A copy of the goal position
     */
    public Position3D getGoalPosition() {
        if (goal_pos == null)
        {
            goal_pos = getPosition();
            return new Position3D(goal_pos);
        }
        else {
            return new Position3D(goal_pos);
        }
    }

    /**
     * Generates a random traversable position on the border of the maze.
     * This is used to determine start and goal positions.
     *
     * @return A randomly generated position
     */
    public Position3D getPosition() {
        int depth_index;
        int row_index;
        int col_index;
        int choice2;
        Random rd = new Random();
        boolean choice;
        while (true)
        {
            choice2 = (int)(Math.random() * 3);
            if (choice2 == 0)
            {
                row_index = (int)(Math.random() * rows);
                col_index = (int)(Math.random() * cols);
                choice = rd.nextBoolean();
                if (choice){
                    if (maze[0][row_index][col_index] == 0){
                        return new Position3D(0, row_index, col_index);
                    }
                } else{
                    if (maze[depth - 1][row_index][col_index] == 0){
                        return new Position3D(depth - 1, row_index, col_index);
                    }
                }
            }
            else if (choice2 == 1){
                depth_index = (int)(Math.random() * depth);
                col_index = (int)(Math.random() * cols);
                choice = rd.nextBoolean();
                if (choice) {
                    if (maze[depth_index][0][col_index] == 0) {
                        return new Position3D(depth_index, 0, col_index);
                    }
                } else {
                    if (maze[depth_index][rows-1][col_index] == 0) {
                        return new Position3D(depth_index, rows-1, col_index);
                    }
                }
            } else {
                depth_index = (int)(Math.random() * depth);
                row_index = (int)(Math.random() * rows);
                choice = rd.nextBoolean();
                if (choice) {
                    if (maze[depth_index][row_index][0] == 0) {
                        return new Position3D(depth_index, row_index, 0);
                    }
                } else {
                    if (maze[depth_index][row_index][cols-1] == 0) {
                        return new Position3D(depth_index, row_index, cols-1);
                    }
                }
            }
        }
    }
}
