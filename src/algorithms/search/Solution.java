package algorithms.search;

import java.util.ArrayList;

public class Solution {
    private final ArrayList<AState> solutionPath;
    public Solution(ArrayList<AState> path)
    {
        this.solutionPath = path;
    }
    public ArrayList<AState> getSolutionPath(){
        return solutionPath;
    }
}
