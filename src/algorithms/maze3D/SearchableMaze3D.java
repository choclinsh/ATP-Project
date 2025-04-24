package algorithms.maze3D;

import algorithms.maze3D.Maze3DState;
import algorithms.search.AState;
import algorithms.search.ISearchable;


import java.util.ArrayList;
/**
 * Adapts a 3D maze to implement the ISearchable interface.
 * This allows search algorithms to be applied to 3D mazes.
 */
public class SearchableMaze3D implements ISearchable {
    private Maze3D maze;

    /**
     * Constructs a searchable 3D maze from a Maze3D object.
     *
     * @param maze The 3D maze to be made searchable
     */
    public SearchableMaze3D(Maze3D maze){
        this.maze = maze;
    }

    /**
     * Gets the start state of the search problem.
     *
     * @return The start state as a Maze3DState
     */
    public AState getStartState(){
        return new Maze3DState(maze.getStartPosition());
    }

    /**
     * Gets the goal state of the search problem.
     *
     * @return The goal state as a Maze3DState
     */
    public AState getGoalState(){
        return new Maze3DState(maze.getGoalPosition());
    }

    /**
     * Adds the six adjacent neighbors of a state to the successors list.
     * Checks up, down, left, right, forward, and backward positions.
     *
     * @param s The current 3D maze state
     * @param successors_list The list to add successors to
     */
    private void addSides(Maze3DState s, ArrayList<AState> successors_list) {
        if (s.getRowIndex() > 0 && maze.getCell(s.getDepthIndex(), s.getRowIndex() - 1, s.getColIndex()) == 0) {
            successors_list.add(new Maze3DState(new Position3D(s.getDepthIndex(), s.getRowIndex() - 1, s.getColIndex()), s, 10));
        }

        if (s.getRowIndex() < maze.getRows() - 1 && maze.getCell(s.getDepthIndex(), s.getRowIndex() + 1, s.getColIndex()) == 0) {
            successors_list.add(new Maze3DState(new Position3D(s.getDepthIndex(), s.getRowIndex() + 1, s.getColIndex()), s, 10));
        }

        if (s.getColIndex() > 0 && maze.getCell(s.getDepthIndex(), s.getRowIndex(), s.getColIndex() - 1) == 0) {
            successors_list.add(new Maze3DState(new Position3D(s.getDepthIndex(), s.getRowIndex(), s.getColIndex() - 1), s, 10));
        }

        if (s.getColIndex() < maze.getCols() - 1 && maze.getCell(s.getDepthIndex(), s.getRowIndex(), s.getColIndex() + 1) == 0) {
            successors_list.add(new Maze3DState(new Position3D(s.getDepthIndex(), s.getRowIndex(), s.getColIndex() + 1), s, 10));
        }

        if (s.getDepthIndex() > 0 && maze.getCell(s.getDepthIndex() - 1, s.getRowIndex(), s.getColIndex()) == 0) {
            successors_list.add(new Maze3DState(new Position3D(s.getDepthIndex() - 1, s.getRowIndex(), s.getColIndex()), s, 10));
        }

        if (s.getDepthIndex() < maze.getDepth() - 1 && maze.getCell(s.getDepthIndex() + 1, s.getRowIndex(), s.getColIndex()) == 0) {
            successors_list.add(new Maze3DState(new Position3D(s.getDepthIndex() + 1, s.getRowIndex(), s.getColIndex()), s, 10));
        }
    }

    /**
     * Gets all possible states that can be reached from the given state.
     * Includes all valid neighboring positions in the six directions.
     *
     * @param state The current state
     * @return A list of all possible states that can be reached
     */
    @Override
    public ArrayList<AState> getAllPossibleStates(AState state) {
        Maze3DState s = (Maze3DState) state;
        ArrayList<AState> successors_list = new ArrayList<>();

        if (s == null || s.getRowIndex() >= maze.getRows() ||
                s.getColIndex() >= maze.getCols() || s.getDepthIndex() >= maze.getDepth()) {
            return successors_list;
        }

        addSides(s, successors_list);
        return successors_list;
    }

}