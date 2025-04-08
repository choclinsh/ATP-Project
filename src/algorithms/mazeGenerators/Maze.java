package algorithms.mazeGenerators;

public class Maze {
    private int[][] maze;
    private int rows;
    private int cols;

    public Maze(int rows, int cols){
        this.maze = new int[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public int[][] getMaze(){
        return this.maze;
    }
    public int getCell(int row, int col){
        return maze[row][col];
    }
    public int getRows(){
        return this.rows;
    }
    public int getCols(){
        return this.cols;
    }
}
