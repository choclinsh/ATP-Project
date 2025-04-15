package algorithms.search;

import java.util.PriorityQueue;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{
    protected PriorityQueue<AState> openList;
    private int evaluatedNodes;

    public ASearchingAlgorithm(){
        openList = new PriorityQueue<AState>();
        evaluatedNodes = 0;
    }

    protected AState popOpenList(){
        evaluatedNodes++;
        return openList.poll();
    }
    @Override
    public Solution solve(ISearchable s) {
        return null;
    }
    @Override
    public int getNumberOfNodesEvaluated(){
        return 0;
    }
    public String getName(){
        return null;
    }
}
