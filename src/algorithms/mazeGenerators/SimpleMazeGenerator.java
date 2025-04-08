package algorithms.mazeGenerators;
import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator{
    public SimpleMazeGenerator(int rows, int cols){
        super(rows, cols);
    }

    public Maze generate(int rows, int cols){
        Maze new_maze = new Maze(rows, cols);
        int[][] maze = new_maze.getMaze();
        Random rd = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = rd.nextBoolean() ? 1 : 0;;
            }
        }
        return new_maze;
    }
}

