package algorithms.search;

public abstract class AState {
    protected String state;
    protected double cost;
    protected AState cameFrom;

    public AState(String state){
        this.state = state;
    }
    public boolean equals(AState s){
        return state.equals(s.state);
    }
    public void setCost(double cost){
        this.cost = cost;
    }
    public void setCameFrom(AState father){
        this.cameFrom = father;
    }
}
