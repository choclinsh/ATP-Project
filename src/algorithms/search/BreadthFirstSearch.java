package algorithms.search;

import java.util.*;

public class BreadthFirstSearch extends ASearchingAlgorithm{
    protected Set<AState> visited;
    private Queue<AState> openList;

    public BreadthFirstSearch(){
        super();
        visited = new HashSet<>();
        openList = new LinkedList<>();
    }

    @Override
    protected AState popOpenList(){
        evaluatedNodes++;
        return openList.poll();
    }

    @Override
    public Solution solve(ISearchable s) {
        if (s == null) {
            return null;
        }
        visited.add(s.getStartState());
        openList.add(s.getStartState());

        AState goalState = s.getGoalState();
        ArrayList<AState> neighbors;
        AState currentState;

        while (!openList.isEmpty()) {
            currentState = popOpenList();

            visited.add(currentState);
            neighbors = s.getAllPossibleStates(currentState);

            for (AState neighbor : neighbors) {

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
                    visited.add(neighbor);
                    openList.add(neighbor);
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
        return "Breadth first search";
    }
}
