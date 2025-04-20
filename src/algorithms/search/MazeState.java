package algorithms.search;

import algorithms.mazeGenerators.Position;

public class MazeState extends AState{


    public MazeState(Position pos){
        super(pos.toString());
        this.cameFrom = null; // check if new is better
        this.cost = 0;
    }

    public MazeState(Position pos, AState father, int cost){
        super(pos.toString());
        this.cameFrom = father;
        this.cost = cost;
    }

    public int getRowIndex(){
        int startIndex = state.indexOf('{') + 1;
        int endIndex = state.indexOf(',');

        String rowValue = state.substring(startIndex, endIndex);
        return Integer.parseInt(rowValue);
    }
    public int getColIndex(){
        int startIndex = state.indexOf(',') + 1;
        int endIndex = state.indexOf('}');

        String rowValue = state.substring(startIndex, endIndex);
        return Integer.parseInt(rowValue);
    }
}
