package algorithms.search;
/**
 * Abstract base class for states in a search problem.
 * Represents a node in the search space.
 */
public abstract class AState {
    protected String state;
    protected double cost;
    protected AState cameFrom;
    /**
     * Creates a new state with the specified representation.
     *
     * @param state String representation of the state
     */
    public AState(String state){
        this.state = state;
    }
    /**
     * Checks if two states are equal.
     * States are considered equal if they have the same string representation.
     *
     * @param obj The object to compare with
     * @return true if the states are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // Check if same object reference
        if (this == obj) return true;

        // Check if null or different class
        if (obj == null || getClass() != obj.getClass()) return false;

        // Cast to AState
        AState other = (AState) obj;

        // Compare states
        return state.equals(other.state);
    }
    /**
     * Generates a hash code for the state.
     * The hash code is based on the string representation of the state.
     *
     * @return The hash code value for the state
     */
    @Override
    public int hashCode() {
        return state.hashCode();
    }
    /**
     * Gets the parent state from which this state was reached.
     *
     * @return The parent state
     */
    public AState getCameFrom(){
        return cameFrom;
    }
    /**
     * Returns the string representation of the state.
     *
     * @return The string representation
     */
    public String toString() {
        return state;
    }
    /**
     * Gets the cost of reaching this state.
     *
     * @return The cost
     */
    public double getCost() {
        return cost;
    }
}
