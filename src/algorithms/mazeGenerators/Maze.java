package algorithms.mazeGenerators;

import java.util.Random;

public class Maze {
    private int[][] maze;
    private int rows;
    private int cols;
    private Position start_pos;
    private Position goal_pos;

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