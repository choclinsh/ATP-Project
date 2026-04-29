package algorithms.mazeGenerators;
/**
 * Generates a maze with no walls (all cells are traversable).
 * This is the simplest maze generator implementation.
 */
public class EmptyMazeGenerator extends AMazeGenerator{
    /**
     * Constructor for the empty maze generator.
     */
    public EmptyMazeGenerator(){
        super();
    }
    /**
     * Generates an empty maze with the specified dimensions.
     * All cells in the maze will be traversable (0).
     *
     * @param rows The number of rows in the maze
     * @param cols The number of columns in the maze
     * @return A new Maze object where all cells are traversable, or null if dimensions are invalid
     */
    public Maze generate(int rows, int cols){
        if (rows < 2 || cols < 2)
        {
            return null;
        }
        Maze new_maze = new Maze(rows, cols);
        int[][] maze = new_maze.getMaze();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 0;
            }
        }
        new_maze.getStartPosition();  // decide the start and goal position. next times that this func is called, return
        // a deep copy of position
        new_maze.getGoalPosition();
        return new_maze;
    }
}
