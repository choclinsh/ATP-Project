package algorithms.search;

import java.util.*;

public class BestFirstSearch extends BreadthFirstSearch{
    private PriorityQueue<AState> openList;

    public BestFirstSearch() {
        super();
        visited = new HashSet<>();
        openList = new PriorityQueue<>(new Comparator<AState>() {
            @Override
            public int compare(AState s1, AState s2) {
                return Double.compare(s1.getCost(), s2.getCost());
            }
        });
    }

    @Override
    public String getName(){
        return "Best first search";
    }
}
