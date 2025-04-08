package algorithms.mazeGenerators;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyMazeGenerator extends AMazeGenerator{
    public MyMazeGenerator(int rows, int cols){
        super(rows, cols);
    }
    private void add_adj_walls(Position current, List<Position> list, Maze maze) {
        int curr_row = current.getRowIndex();
        int curr_col = current.getColumnIndex();
        if (curr_row > 0) {
            if (maze.getCell(curr_row - 1, curr_col) == 1) {
                list.add(new Position(curr_row - 1, curr_col));
            }
        }
        if (curr_row < maze.getRows() - 1) {
            if (maze.getCell(curr_row + 1, curr_col) == 1) {
                list.add(new Position(curr_row - 1, curr_col));
            }
        }
        if (curr_col > 0) {
            if (maze.getCell(curr_row, curr_col - 1) == 1) {
                list.add(new Position(curr_row - 1, curr_col));
            }
        }
        if (curr_col < maze.getCols() - 1) {
            if (maze.getCell(curr_row, curr_col + 1) == 1) {
                list.add(new Position(curr_row - 1, curr_col));
            }
        }
    }

    public Maze generate(int rows, int cols){
        Maze new_maze = new Maze(rows, cols);
        int[][] maze = new_maze.getMaze();
        // Start with a grid full of walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 1;
            }
        }
        // Pick a cell, mark it as part of the maze.
        int row_index = (int)(Math.random() * rows);
        int col_index = (int)(Math.random() * cols);
        Position first_position = new Position(row_index, col_index);
        maze[row_index][col_index] = 0;

        //Add the walls of the cell to the wall list.
        List<Position> adjacent_walls = new ArrayList<Position>();
        add_adj_walls(first_position,adjacent_walls,new_maze);
        int index_chosen_in_list;
        Position chosen_pos;
        int adj_visit_counter = 0;
        //While there are walls in the list:
        while (!adjacent_walls.isEmpty()){
            index_chosen_in_list = (int)(Math.random() * adjacent_walls.size());
            chosen_pos = adjacent_walls.get(index_chosen_in_list);
            if (chosen_pos.getRowIndex() > 0) {
                if (maze[chosen_pos.getRowIndex()-1][chosen_pos.getColumnIndex()] == 0) {
                    adj_visit_counter++;
                }
            }
            if (chosen_pos.getRowIndex() < new_maze.getRows() - 1) {
                if (maze[chosen_pos.getRowIndex()+1][chosen_pos.getColumnIndex()] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter ==2) {
                        adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
            if (chosen_pos.getColumnIndex() > 0) {
                if (maze[chosen_pos.getRowIndex()][chosen_pos.getColumnIndex()-1] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter ==2) {
                        adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
            if (chosen_pos.getColumnIndex() < new_maze.getCols() - 1) {
                if (maze[chosen_pos.getRowIndex()][chosen_pos.getColumnIndex()+1] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter ==2) {
                        adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
        }
        return new_maze;
    }
}
