package algorithms.maze3D;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * A concrete 3D maze generator using a randomized algorithm.
 */
public class MyMaze3DGenerator extends AMaze3DGenerator {
    /**
     * Default constructor for MyMaze3DGenerator.
     */
    public MyMaze3DGenerator() {
        super();
    }

    /**
     * Adds adjacent wall positions to the wall  list.
     *
     * @param current current position in the maze.
     * @param list list to store adjacent wall positions.
     * @param maze reference to the maze.
     */
    private void add_adj_walls(Position3D current, List<Position3D> list, Maze3D maze) {
        int curr_depth = current.getDepthIndex();
        int curr_row = current.getRowIndex();
        int curr_col = current.getColumnIndex();
        if (curr_depth > 0 && maze.getCell(curr_depth - 1, curr_row, curr_col) == 1)
            list.add(new Position3D(curr_depth - 1, curr_row, curr_col));
        if (curr_depth < maze.getDepth() - 1 && maze.getCell(curr_depth + 1, curr_row, curr_col) == 1)
            list.add(new Position3D(curr_depth + 1, curr_row, curr_col));
        if (curr_row > 0 && maze.getCell(curr_depth, curr_row - 1, curr_col) == 1)
            list.add(new Position3D(curr_depth, curr_row - 1, curr_col));
        if (curr_row < maze.getRows() - 1 && maze.getCell(curr_depth, curr_row + 1, curr_col) == 1)
            list.add(new Position3D(curr_depth, curr_row + 1, curr_col));
        if (curr_col > 0 && maze.getCell(curr_depth, curr_row, curr_col - 1) == 1)
            list.add(new Position3D(curr_depth, curr_row, curr_col - 1));
        if (curr_col < maze.getCols() - 1 && maze.getCell(curr_depth, curr_row, curr_col + 1) == 1)
            list.add(new Position3D(curr_depth, curr_row, curr_col + 1));

    }

    /**
     * Generates a 3D maze with the specified dimensions.
     *
     * @param depth the number of depth layers.
     * @param rows the number of rows.
     * @param cols the number of columns.
     * @return generated Maze3D object.
     */
    @Override
    public Maze3D generate(int depth, int rows, int cols) {
        Maze3D new_maze = new Maze3D(depth, rows, cols);
        int[][][] maze = new_maze.getMap();
        // Start with a grid full of walls
        for (int d = 0; d < depth; d++) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    maze[d][r][c] = 1;
                }
            }
        }
        // Pick a cell, mark it as part of the maze.
        int depth_index = (int) (Math.random() * depth);
        int row_index = (int) (Math.random() * rows);
        int col_index = (int) (Math.random() * cols);
        Position3D first_position = new Position3D(depth_index, row_index, col_index);
        maze[depth_index][row_index][col_index] = 0;

        //Add the walls of the cell to the wall list.
        List<Position3D> adjacent_walls = new ArrayList<>();
        add_adj_walls(first_position, adjacent_walls, new_maze);

        creationLoop(adjacent_walls, maze, new_maze);
        new_maze.getStartPosition();  // decide the start and goal position. next times that this func is called, return
        // a deep copy of position
        new_maze.getGoalPosition();
        return new_maze;
    }

    /**
     * Carves out paths in the maze from the wall list.
     *
     * @param adjacent_walls the list of adjacent walls.
     * @param maze the maze map.
     * @param new_maze reference to the Maze3D object.
     */
    private void creationLoop(List<Position3D> adjacent_walls, int[][][] maze, Maze3D new_maze) {
        int index_chosen_in_list;
        Position3D chosen_pos;
        int adj_visit_counter = 0;
        while (!adjacent_walls.isEmpty()) {
            index_chosen_in_list = (int) (Math.random() * adjacent_walls.size());
            chosen_pos = adjacent_walls.get(index_chosen_in_list);
            if (chosen_pos.getDepthIndex() > 0) {
                if (maze[chosen_pos.getDepthIndex() - 1][chosen_pos.getRowIndex()][chosen_pos.getColumnIndex()] == 0) {
                    adj_visit_counter++;
                }
            }
            if (chosen_pos.getDepthIndex() < new_maze.getDepth() - 1) {
                if (maze[chosen_pos.getDepthIndex() + 1][chosen_pos.getRowIndex()][chosen_pos.getColumnIndex()] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter == 2) {
                        adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
            if (chosen_pos.getRowIndex() > 0 && adj_visit_counter < 2) {
                if (maze[chosen_pos.getDepthIndex()][chosen_pos.getRowIndex() - 1][chosen_pos.getColumnIndex()] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter == 2) {
                        adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
            if (chosen_pos.getRowIndex() < new_maze.getDepth() - 1) {
                if (maze[chosen_pos.getDepthIndex()][chosen_pos.getRowIndex() + 1][chosen_pos.getColumnIndex()] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter == 2) {
                        adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
            if (chosen_pos.getColumnIndex() > 0 && adj_visit_counter < 2) {
                if (maze[chosen_pos.getDepthIndex()][chosen_pos.getRowIndex()][chosen_pos.getColumnIndex() - 1] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter == 2) {
                        adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
            if (chosen_pos.getColumnIndex() < new_maze.getCols() - 1 && adj_visit_counter < 2) {
                if (maze[chosen_pos.getDepthIndex()][chosen_pos.getRowIndex()][chosen_pos.getColumnIndex() + 1] == 0) {
                    adj_visit_counter++;
                    if (adj_visit_counter == 2) {
                        adjacent_walls.remove(index_chosen_in_list);
                    }
                }
            }
            if (adj_visit_counter < 2) {
                maze[chosen_pos.getDepthIndex()][chosen_pos.getRowIndex()][chosen_pos.getColumnIndex()] = 0;
                add_adj_walls(chosen_pos, adjacent_walls, new_maze);
                adjacent_walls.remove(index_chosen_in_list);
            }
            adj_visit_counter = 0;
        }
    }
}
