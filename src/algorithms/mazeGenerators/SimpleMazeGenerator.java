package algorithms.mazeGenerators;
import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator{
    public SimpleMazeGenerator(){
        super();
    }

    public Maze generate(int rows, int cols){
        Maze new_maze = new Maze(rows, cols);
        int[][] maze = new_maze.getMaze();
        Random rd = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = rd.nextBoolean() ? 1 : 0;
            }
        }
        new_maze.getStartPosition();  // decide the start and goal position. next times that this func is called, return
        // a deep copy of position
        new_maze.getGoalPosition();
        return new_maze;
    }
}

