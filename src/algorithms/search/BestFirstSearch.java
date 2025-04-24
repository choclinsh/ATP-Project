package algorithms.search;

import java.util.*;
/**
 * Implementation of the Best-First Search algorithm.
 * Uses a priority queue to explore nodes in order of their cost.
 */
public class BestFirstSearch extends BreadthFirstSearch{
    private PriorityQueue<AState> openList;
    /**
     * Creates a new Best-First Search instance.
     * Initializes the visited set and open list priority queue sorted by cost instead .
     */
    public BestFirstSearch() {
        super();
        visited = new HashSet<>();
        openList = new PriorityQueue<>(new Comparator<AState>() {
            @Override
            public int compare(AState s1, AState s2) {
                return Double.compare(s1.getCost(), s2.getCost());
            }
        });
    }
    /**
     * Solves a searchable problem using Best-First Search.
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
        // Add start state to visited set and open list
        visited.add(s.getStartState());
        openList.add(s.getStartState());

        // Get the goal state to compare with
        AState goalState = s.getGoalState();
        ArrayList<AState> neighbors;
        AState currentState;

        // Main BFS loop - continue until open list is empty
        while (!openList.isEmpty()) {
            // Get next state from open list
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
     * Pops the next state from the open list (lowest cost).
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
     * Gets the name of the algorithm.
     *
     * @return The name "Best first search"
     */
    @Override
    public String getName(){
        return "Best first search";
    }
}
