package algorithms.search;

public abstract class AState {
    protected String state;
    protected double cost;
    protected AState cameFrom;

    public AState(String state){
        this.state = state;
    }

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

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    public AState getCameFrom(){
        return cameFrom;
    }

    public String toString() {
        return state;
    }

    public double getCost() {
        return cost;
    }
}
