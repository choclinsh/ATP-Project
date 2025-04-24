package algorithms.search;
import java.util.ArrayList;
/**
 * Interface representing a problem that can be searched.
 */
public interface ISearchable {
    /**
     * Gets the start state of the problem.
     *
     * @return The start state
     */
    AState getStartState();
    /**
     * Gets the goal state of the problem.
     *
     * @return The goal state
     */
    AState getGoalState();
    /**
     * Gets all possible states that can be reached from the given state.
     *
     * @param state The current state
     * @return A list of all possible states that can be reached
     */
    ArrayList<AState> getAllPossibleStates (AState state);
}
