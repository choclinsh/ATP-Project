package algorithms.mazeGenerators;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a complex maze using a randomized Prim's algorithm variant.
 * This creates a maze with corridors and walls where there is always at least
 * one path from start to goal.
 */
public class MyMazeGenerator extends AMazeGenerator{
    /**
     * Constructor for the complex maze generator.
     */
    public MyMazeGenerator(){
        super();
    }
    /**
     * Adds adjacent walls of a position to the provided list.
     *
     * @param current Current position
     * @param list List to add adjacent walls to
     * @param maze The maze object
     */
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
                list.add(new Position(curr_row + 1, curr_col));
            }
        }
        if (curr_col > 0) {
            if (maze.getCell(curr_row, curr_col - 1) == 1) {
                list.add(new Position(curr_row, curr_col-1));
            }
        }
        if (curr_col < maze.getCols() - 1) {
            if (maze.getCell(curr_row, curr_col + 1) == 1) {
                list.add(new Position(curr_row, curr_col+1));
            }
        }
    }
    /**
     * Generates a complex maze with the specified dimensions using a
     * randomized Prim's algorithm variant.
     *
     * @param rows The number of rows in the maze
     * @param cols The number of columns in the maze
     * @return A new Maze object with a complex structure, or null if dimensions are invalid
     */
    public Maze generate(int rows, int cols){
        if (rows < 2 || cols < 2)
        {
            return null;
        }
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
        List<Position> adjacent_walls = new ArrayList<>();
        add_adj_walls(first_position,adjacent_walls,new_maze);

        creationLoop(adjacent_walls, maze, new_maze);
        new_maze.getStartPosition();  // decide the start and goal position. next times that this func is called, return
        // a deep copy of position
        new_maze.getGoalPosition();
        return new_maze;
    }
    /**
     * Main loop for the maze creation algorithm. Processes walls and creates paths.
     *
     * @param adjacent_walls List of walls to process
     * @param maze The maze 2D array
     * @param new_maze The maze object
     */
    private void creationLoop(List<Position> adjacent_walls, int[][] maze, Maze new_maze){
        int index_chosen_in_list;
        Position chosen_pos;
        int adj_visit_counter = 0;
        while (!adjacent_walls.isEmpty()){
            index_chosen_in_list = (int)(Math.random() * adjacent_walls.size());  // picks random cell from list
            chosen_pos = adjacent_walls.get(index_chosen_in_list);
            if (chosen_pos.getRowIndex() > 0) {  // checks for max 2 accessible neighbors and then removes the origin from the list
                if (maze[chosen_pos.getRowIndex()-1][chosen_pos.getColumnIndex()] == 0) {adj_visit_counter++;}
            }
            if (chosen_pos.getRowIndex() < new_maze.getRows() - 1) {  // every time validates the location is not out of index
                if (maze[chosen_pos.getRowIndex()+1][chosen_pos.getColumnIndex()] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter ==2) {adjacent_walls.remove(index_chosen_in_list);}
                }
            }
            if (chosen_pos.getColumnIndex() > 0 && adj_visit_counter < 2) {
                if (maze[chosen_pos.getRowIndex()][chosen_pos.getColumnIndex()-1] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter ==2) {adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
            if (chosen_pos.getColumnIndex() < new_maze.getCols() - 1 && adj_visit_counter < 2) {
                if (maze[chosen_pos.getRowIndex()][chosen_pos.getColumnIndex()+1] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter ==2) {adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
            if (adj_visit_counter < 2) {
                maze[chosen_pos.getRowIndex()][chosen_pos.getColumnIndex()] = 0;
                add_adj_walls(chosen_pos,adjacent_walls,new_maze);
                adjacent_walls.remove(index_chosen_in_list);
            }
            adj_visit_counter = 0;
        }
    }
}