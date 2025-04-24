package algorithms.search;

import java.util.*;
/**
 * Implementation of the Breadth-First Search algorithm.
 * Uses a queue to explore nodes in order of their distance from the start state.
 */
public class BreadthFirstSearch extends ASearchingAlgorithm{
    protected Set<AState> visited;
    private Queue<AState> openList;
    /**
     * Creates a new Breadth-First Search instance.
     * Initializes the visited set and open list queue.
     */
    public BreadthFirstSearch(){
        super();
        visited = new HashSet<>();
        openList = new LinkedList<>();
    }
    /**
     * Pops the next state from the open list (queue front).
     * Increments the evaluated nodes counter.
     *
     * @return The next state to be evaluated
     */
    @Override
    protected AState popOpenList(){
        evaluatedNodes++;
        return openList.poll();
    }
    /**
     * Solves a searchable problem using Breadth-First Search.
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

        // Add start state to visited set and priority queue (sorted by cost)
        visited.add(s.getStartState());
        openList.add(s.getStartState());

        // Get the goal state to compare with
        AState goalState = s.getGoalState();
        ArrayList<AState> neighbors;
        AState currentState;

        // Main Best-First Search loop - continue until open list is empty
        while (!openList.isEmpty()) {
            // Get state with lowest cost from priority queue
            currentState = popOpenList();

            // Mark current state as visited
            visited.add(currentState);

            // Get all possible next states (neighbors)
            neighbors = s.getAllPossibleStates(currentState);

            // Process each neighbor
            for (AState neighbor : neighbors) {
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

                // If neighbor hasn't been visited, add to visited set and open list
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    openList.add(neighbor);
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
     * @return The name "Breadth first search"
     */
    public String getName(){
        return "Breadth first search";
    }
}
