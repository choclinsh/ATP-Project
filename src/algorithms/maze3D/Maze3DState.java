package algorithms.maze3D;

import algorithms.search.AState;

/**
 * Represents a state in a 3D maze search problem.
 * Extends the abstract AState class to include 3D position information.
 */
public class Maze3DState extends AState{
    /**
     * Constructs a new maze state for the given 3D position.
     * Initializes with no parent and zero cost.
     *
     * @param pos The position in the 3D maze
     */
    public Maze3DState(Position3D pos){
        super(pos.toString());
        this.cameFrom = null; // check if new is better
        this.cost = 0;
    }

    /**
     * Constructs a new maze state with the specified position, parent, and cost.
     *
     * @param pos The position in the 3D maze
     * @param father The parent state
     * @param cost The cost to reach this state
     */
    public Maze3DState(Position3D pos, AState father, int cost){
        super(pos.toString());
        this.cameFrom = father;
        this.cost = cost;
    }


    /**
     * Extracts the depth index from the state string representation.
     *
     * @return The depth index
     */
    public int getDepthIndex(){
        int startIndex = state.indexOf('{') + 1;
        int endIndex = state.indexOf(',');

        String depthValue = state.substring(startIndex, endIndex);
        return Integer.parseInt(depthValue);
    }

    /**
     * Extracts the row index from the state string representation.
     *
     * @return The row index
     */
    public int getRowIndex(){
        String content = state.substring(1, state.length() - 1);
        String[] parts = content.split(",");
        return Integer.parseInt(parts[1]);
    }

    /**
     * Extracts the column index from the state string representation.
     *
     * @return The column index
     */
    public int getColIndex(){
        String content = state.substring(1, state.length() - 1);
        String[] parts = content.split(",");
        return Integer.parseInt(parts[2]);
    }
}