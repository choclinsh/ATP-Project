package algorithms.search;

import java.util.ArrayList;
/**
 * Represents a solution to a search problem.
 * Contains the path from the start state to the goal state.
 */
public class Solution {
    private final ArrayList<AState> solutionPath;
    /**
     * Creates a new solution with the specified path.
     *
     * @param path The path from start to goal
     */
    public Solution(ArrayList<AState> path)
    {
        this.solutionPath = path;
    }
    /**
     * Gets the solution path.
     *
     * @return The path from start to goal
     */
    public ArrayList<AState> getSolutionPath(){
        return solutionPath;
    }
}
