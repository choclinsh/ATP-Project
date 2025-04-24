package algorithms.search;
/**
 * Abstract base class for searching algorithms.
 * Provides common functionality for search algorithms.
 */
public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{
    protected int evaluatedNodes;
    /**
     * Creates a new searching algorithm instance.
     * Initializes the evaluated nodes counter to 0.
     */
    public ASearchingAlgorithm(){
        evaluatedNodes = 0;
    }
    /**
     * Pops the next state from the open list to be evaluated.
     * The implementation depends on the specific search algorithm.
     *
     * @return The next state to be evaluated
     */
    protected abstract AState popOpenList();
    /**
     * Solves a searchable problem.
     * This method is intended to be overridden by subclasses.
     *
     * @param s The searchable problem to solve
     * @return A solution containing the path from start to goal, or null if no solution exists
     */
    @Override
    public Solution solve(ISearchable s) {
        return null;
    }
    /**
     * Gets the number of nodes evaluated during the search.
     *
     * @return The number of evaluated nodes
     */
    @Override
    public int getNumberOfNodesEvaluated(){
        return 0;
    }
    /**
     * Gets the name of the searching algorithm.
     * This method is intended to be overridden by subclasses.
     *
     * @return The name of the algorithm
     */
    public String getName(){
        return null;
    }
}
