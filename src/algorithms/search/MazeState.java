package algorithms.search;

import algorithms.mazeGenerators.Position;

public class MazeState extends AState{


    public MazeState(Position pos){
        super(pos.toString());
        this.cameFrom = null; // check if new is better
    }

    public MazeState(Position pos, AState father){
        super(pos.toString());
        this.cameFrom = father; // check if new is better
    }

    public int getRowIndex(){
        return Character.getNumericValue(state.charAt(1));
    }
    public int getColIndex(){
        return Character.getNumericValue(state.charAt(3));
    }
}
