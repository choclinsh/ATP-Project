package algorithms.mazeGenerators;

import java.util.Random;
/**
 * Represents a 2D maze with walls, paths, start position, and goal position.
 * The maze consists of a 2D array where 0 represents traversable paths and 1 represents walls.
 */
public class Maze {
    private final int[][] maze;
    private final int rows;
    private final int cols;
    private Position start_pos;
    private Position goal_pos;
    /**
     * Creates a maze with the specified dimensions.
     *
     * @param rows The number of rows in the maze
     * @param cols The number of columns in the maze
     */
    public Maze(int rows, int cols){
        this.maze = new int[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }
    /**
     * Gets the 2D array representing the maze.
     *
     * @return The 2D maze array
     */
    public int[][] getMaze(){
        return this.maze;
    }
    /**
     * Gets the value of a specific cell in the maze.
     *
     * @param row The row index
     * @param col The column index
     * @return The cell value (0 for path, 1 for wall)
     */
    public int getCell(int row, int col){
        return maze[row][col];
    }
    /**
     * Gets the number of rows in the maze.
     *
     * @return The number of rows
     */
    public int getRows(){
        return this.rows;
    }
    /**
     * Gets the number of columns in the maze.
     *
     * @return The number of columns
     */
    public int getCols(){
        return this.cols;
    }
    /**
     * Prints the maze to the console.
     * Uses 'S' to mark the start position and 'E' to mark the end position.
     */
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == start_pos.getRowIndex() && j == start_pos.getColumnIndex()) {
                    System.out.printf("%4c", 'S');
                } else if (i == goal_pos.getRowIndex() && j == goal_pos.getColumnIndex()) {
                    System.out.printf("%4c", 'E');
                } else {
                    System.out.printf("%4d", maze[i][j]);
                }
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
    public Position getStartPosition() {
        if (start_pos == null)
        {
            start_pos = getPosition();
            return new Position(start_pos);
        }
        else {
            return new Position(start_pos);
        }
    }
    /**
     * Gets the goal position of the maze.
     * Initializes the goal position if not already set.
     *
     * @return A copy of the goal position
     */
    public Position getGoalPosition() {
        if (goal_pos == null)
        {
            goal_pos = getPosition();
            return new Position(goal_pos);
        }
        else {
            return new Position(goal_pos);
        }
    }
    /**
     * Generates a random position on the border of the maze that is traversable (value 0).
     *
     * @return A randomly generated position
     */
    public Position getPosition() {
        int row_index;
        int col_index;
        Random rd = new Random();
        boolean choice;
        while (true)
        {
            choice = rd.nextBoolean();
            if (choice)
            {
                row_index = (int)(Math.random() * rows);
                choice = rd.nextBoolean();
                if (choice){
                    if (maze[row_index][0] == 0){
                        return new Position(row_index, 0);
                    }
                } else{
                    if (maze[row_index][cols-1] == 0){
                        return new Position(row_index, cols-1);
                    }
                }
            }
            else {
                col_index = (int)(Math.random() * cols);
                choice = rd.nextBoolean();
                if (choice) {
                    if (maze[0][col_index] == 0){
                        return new Position(0, col_index);
                    }
                }
                else {
                    if (maze[rows-1][col_index] == 0){
                        return new Position(rows-1, col_index);
                    }
                }
            }
        }
    }
}