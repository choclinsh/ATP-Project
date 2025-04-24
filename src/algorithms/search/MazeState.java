package algorithms.search;

import algorithms.mazeGenerators.Position;
/**
 * Represents a state in a maze search problem.
 * Encapsulates a position in the maze along with cost and parent information.
 */
public class MazeState extends AState{
    /**
     * Creates a new maze state for the given position.
     * Initializes with no parent and zero cost.
     *
     * @param pos The position in the maze
     */
    public MazeState(Position pos){
        super(pos.toString());
        this.cameFrom = null; // check if new is better
        this.cost = 0;
    }
    /**
     * Creates a new maze state with the specified position, parent, and cost.
     *
     * @param pos The position in the maze
     * @param father The parent state
     * @param cost The cost to reach this state
     */
    public MazeState(Position pos, AState father, int cost){
        super(pos.toString());
        this.cameFrom = father;
        this.cost = cost;
    }
    /**
     * Extracts the row index from the state string representation.
     *
     * @return The row index
     */
    public int getRowIndex(){
        int startIndex = state.indexOf('{') + 1;
        int endIndex = state.indexOf(',');

        String rowValue = state.substring(startIndex, endIndex);
        return Integer.parseInt(rowValue);
    }/**
     * Extracts the column index from the state string representation.
     *
     * @return The column index
     */
    public int getColIndex(){
        int startIndex = state.indexOf(',') + 1;
        int endIndex = state.indexOf('}');

        String rowValue = state.substring(startIndex, endIndex);
        return Integer.parseInt(rowValue);
    }
}
