package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;
/**
 * Adapts a Maze to implement the ISearchable interface.
 * Allows search algorithms to be applied to mazes.
 */
public class SearchableMaze implements ISearchable{
    private Maze maze;
    /**
     * Creates a new searchable maze.
     *
     * @param maze The maze to be searched
     * @throws IllegalArgumentException if the maze is null
     */
    public SearchableMaze(Maze maze){
        if (maze == null) {
            throw new IllegalArgumentException("Maze cannot be null");
        }
        this.maze = maze;
    }
    /**
     * Gets the start state of the maze.
     *
     * @return The start state
     */
    public AState getStartState(){
        return new MazeState(maze.getStartPosition());
    }
    /**
     * Gets the goal state of the maze.
     *
     * @return The goal state
     */
    public AState getGoalState(){
        return new MazeState(maze.getGoalPosition());
    }
    /**
     * Gets the maze.
     *
     * @return The maze
     */
    public Maze getMaze(){
        return maze;
    }
    /**
     * Adds the orthogonal neighbors of a state to the successors list.
     * Checks up, right, down, and left positions.
     *
     * @param s The current maze state
     * @param successors_list The list to add successors to
     */
    public void addSides(MazeState s, ArrayList<AState> successors_list) {
        if (s.getRowIndex() > 0) {  // check every time different conditions to prevent indexes out of bounds
            if (maze.getCell((s.getRowIndex() - 1), s.getColIndex()) == 0) {  // the cost of orthogonal moves is 10
                successors_list.add(new MazeState(new Position((s.getRowIndex() - 1), s.getColIndex()),s, 10));
            }
        }
        if (s.getColIndex() < maze.getCols()-1) {
            if (maze.getCell((s.getRowIndex()), s.getColIndex() + 1) == 0) {
                successors_list.add(new MazeState(new Position((s.getRowIndex()), s.getColIndex() + 1),s ,10));  // right
            }
        }
        if (s.getRowIndex() < maze.getRows() - 1) {
            if (maze.getCell((s.getRowIndex() + 1), s.getColIndex()) == 0) {
                successors_list.add(new MazeState(new Position((s.getRowIndex() + 1), s.getColIndex()),s,10));  // down
            }
        }
        if (s.getColIndex() > 0) {
            if (maze.getCell((s.getRowIndex()), s.getColIndex() - 1) == 0) {
                successors_list.add(new MazeState(new Position((s.getRowIndex()), s.getColIndex() - 1),s,10));  // left
            }
        }
    }
    /**
     * Adds the diagonal neighbors of a state to the successors list.
     * Checks up-right, down-right, down-left, and up-left positions.
     * For a diagonal move to be valid one of the sides should be accessible.
     *
     * @param s The current maze state
     * @param successors_list The list to add successors to
     */
    public void addDiagonals(MazeState s, ArrayList<AState> successors_list) {
        if (s.getRowIndex() > 0 && s.getColIndex() < maze.getCols() - 1) {  // check every time different conditions to prevent indexes out of bounds
            if (maze.getCell((s.getRowIndex() - 1), s.getColIndex()) == 0 || maze.getCell((s.getRowIndex()),
                    s.getColIndex() + 1) == 0) {  // the cost of diagonal moves is 15
                if (maze.getCell((s.getRowIndex() - 1), s.getColIndex() + 1) == 0) {  // right up
                    successors_list.add(new MazeState(new Position((s.getRowIndex() - 1), s.getColIndex() + 1), s,15));
                }
            }
        }
        if (s.getRowIndex() < maze.getRows()-1 && s.getColIndex() < maze.getCols()-1) {
            if (maze.getCell((s.getRowIndex() + 1), s.getColIndex()) == 0 || maze.getCell((s.getRowIndex()),
                    s.getColIndex() + 1) == 0) {
                if (maze.getCell((s.getRowIndex() + 1), s.getColIndex() + 1) == 0) {
                    successors_list.add(new MazeState(new Position((s.getRowIndex() + 1), s.getColIndex() + 1), s,15));  // right down
                }
            }
        }
        if (s.getRowIndex() < maze.getRows() - 1 && s.getColIndex() > 0) {
            if (maze.getCell((s.getRowIndex() + 1), s.getColIndex()) == 0 || maze.getCell((s.getRowIndex()),
                    s.getColIndex() - 1) == 0) {
                if (maze.getCell((s.getRowIndex() + 1), s.getColIndex() - 1) == 0) {
                    successors_list.add(new MazeState(new Position((s.getRowIndex() + 1), s.getColIndex() - 1), s,15));  // down left
                }
            }
        }
        if (s.getRowIndex() > 0 && s.getColIndex() > 0) {
            if (maze.getCell((s.getRowIndex() - 1), s.getColIndex()) == 0 || maze.getCell((s.getRowIndex()),
                    s.getColIndex() - 1) == 0) {
                if (maze.getCell((s.getRowIndex() - 1), s.getColIndex() - 1) == 0) {
                    successors_list.add(new MazeState(new Position((s.getRowIndex() - 1), s.getColIndex() - 1), s,15));  // left up
                }
            }
        }
    }
    /**
     * Gets all possible states that can be reached from the given state.
     * Includes both orthogonal and diagonal neighbors.
     *
     * @param state The current state
     * @return A list of all possible states that can be reached
     */
    public ArrayList<AState> getAllPossibleStates (AState state) {
        MazeState s = (MazeState)state;
        ArrayList<AState> successors_list = new ArrayList<>();
        if (s == null || s.getRowIndex() >= maze.getRows() || s.getColIndex() >= maze.getCols()) {
            return successors_list;
        }
        addSides(s, successors_list);
        addDiagonals(s, successors_list);
        return successors_list;
    }
}
