package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator{

    public EmptyMazeGenerator(int rows, int cols){
        super(rows, cols);
    }

    public Maze generate(int rows, int cols){
        Maze new_maze = new Maze(rows, cols);
        int[][] maze = new_maze.getMaze();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 0;
            }
        }
        return new_maze;
    }
}
