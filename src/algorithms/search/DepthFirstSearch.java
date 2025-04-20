package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DepthFirstSearch extends ASearchingAlgorithm{
    private Set<AState> visited;
    private Stack<AState> openList;

    public DepthFirstSearch(){
        super();
        visited = new HashSet<>();
        openList = new Stack<>();
    }

    @Override
    protected AState popOpenList(){
        evaluatedNodes++;
        return openList.pop();
    }

    @Override
    public Solution solve(ISearchable s) {
        openList.push(s.getStartState());
        AState goalState = s.getGoalState();
        ArrayList<AState> neighbors;
        AState neighbor;
        AState currentState;

        while (!openList.isEmpty()) {
            currentState = popOpenList();

            if (!visited.contains(currentState)){
                visited.add(currentState);
                neighbors = s.getAllPossibleStates(currentState);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    neighbor = neighbors.get(i);
                    if (neighbor.equals(goalState)){
                        ArrayList<AState> path = new ArrayList<>();
                        while (neighbor.getCameFrom() != null){
                            path.add(0, neighbor);
                            neighbor = neighbor.cameFrom;
                        }
                        path.add(0, neighbor);
                        return new Solution(path);
                    }

                    if (!visited.contains(neighbor)) {
                        openList.push(neighbor);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getNumberOfNodesEvaluated(){
        return evaluatedNodes;
    }

    public String getName(){
        return "Depth first search";
    }
}
