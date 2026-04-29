package algorithms.search;
/**
 * Interface representing a searching algorithm for solving problems.
 */
public interface ISearchingAlgorithm {
    /**
     * Solves a searchable problem.
     *
     * @param s The searchable problem to solve
     * @return A solution containing the path from start to goal
     */
    Solution solve(ISearchable s);
    /**
     * Gets the number of nodes evaluated during the search.
     *
     * @return The number of evaluated nodes
     */
    int getNumberOfNodesEvaluated();
    /**
     * Gets the name of the searching algorithm.
     *
     * @return The name of the algorithm
     */
    String getName();
}
