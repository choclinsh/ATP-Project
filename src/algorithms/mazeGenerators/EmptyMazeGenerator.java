package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator{

    public EmptyMazeGenerator(){
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
                maze[i][j] = 0;
            }
        }
        new_maze.getStartPosition();  // decide the start and goal position. next times that this func is called, return
        // a deep copy of position
        new_maze.getGoalPosition();
        return new_maze;
    }
}
