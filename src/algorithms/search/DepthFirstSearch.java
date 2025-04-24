package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
/**
 * Implementation of the Depth-First Search algorithm.
 * Uses a stack to explore nodes by expanding the deepest unexpanded node first.
 */
public class DepthFirstSearch extends ASearchingAlgorithm{
    private Set<AState> visited;
    private Stack<AState> openList;
    /**
     * Creates a new Depth-First Search instance.
     * Initializes the visited set and open list stack.
     */
    public DepthFirstSearch(){
        super();
        visited = new HashSet<>();
        openList = new Stack<>();
    }
    /**
     * Pops the next state from the open list (stack top).
     * Increments the evaluated nodes counter.
     *
     * @return The next state to be evaluated
     */
    @Override
    protected AState popOpenList(){
        evaluatedNodes++;
        return openList.pop();
    }
    /**
     * Solves a searchable problem using Depth-First Search.
     *
     * @param s The searchable problem to solve
     * @return A solution containing the path from start to goal, or null if no solution exists
     */
    @Override
    public Solution solve(ISearchable s) {
        // Return null if the searchable problem is null
        if (s == null) {
            return null;
        }

        // For DFS, we add start state only to the stack (not to visited initially)
        openList.push(s.getStartState());

        // Get the goal state to compare with
        AState goalState = s.getGoalState();
        ArrayList<AState> neighbors;
        AState neighbor;
        AState currentState;

        // Main DFS loop - continue until open list is empty
        while (!openList.isEmpty()) {
            // Get next state from stack
            currentState = popOpenList();

            // Only process current state if it hasn't been visited
            if (!visited.contains(currentState)){
                // Mark current state as visited
                visited.add(currentState);

                // Get all possible next states (neighbors)
                neighbors = s.getAllPossibleStates(currentState);

                // Process neighbors in reverse order (for consistent DFS behavior)
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    neighbor = neighbors.get(i);

                    // If we found the goal, construct the path and return solution
                    if (neighbor.equals(goalState)){
                        ArrayList<AState> path = new ArrayList<>();
                        // Backtrack using parent references to build complete path
                        while (neighbor.getCameFrom() != null){
                            path.add(0, neighbor);
                            neighbor = neighbor.cameFrom;
                        }
                        path.add(0, neighbor);
                        return new Solution(path);
                    }

                    // If neighbor hasn't been visited, add to stack
                    if (!visited.contains(neighbor)) {
                        openList.push(neighbor);
                    }
                }
            }
        }

        // No solution found
        return null;
    }
    /**
     * Gets the number of nodes evaluated during the search.
     *
     * @return The number of evaluated nodes
     */
    @Override
    public int getNumberOfNodesEvaluated(){
        return evaluatedNodes;
    }
    /**
     * Gets the name of the algorithm.
     *
     * @return The name "Depth first search"
     */
    public String getName(){
        return "Depth first search";
    }
}
