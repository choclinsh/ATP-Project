package algorithms.search;

import java.util.PriorityQueue;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{
    protected int evaluatedNodes;

    public ASearchingAlgorithm(){
        evaluatedNodes = 0;
    }

    protected abstract AState popOpenList();

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
