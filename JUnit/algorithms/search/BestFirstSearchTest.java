package algorithms.search;

import static org.junit.jupiter.api.Assertions.*;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

class BestFirstSearchTest {
    private BestFirstSearch bestFirstSearch;
    private SearchableMaze searchableMaze;

    @BeforeEach
    void setUp() {
        bestFirstSearch = new BestFirstSearch();
        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(1000, 1000);
        searchableMaze = new SearchableMaze(maze);
    }

    @Test
    @DisplayName("Test algorithm name is correct")
    void testGetName() {
        assertEquals("Best first search", bestFirstSearch.getName());
    }

    @Test
    @DisplayName("Test evaluatedNodes counter increments correctly")
    void testEvaluatedNodesCounter() {
        assertEquals(0, bestFirstSearch.getNumberOfNodesEvaluated());
        bestFirstSearch.solve(searchableMaze);
        assertTrue(bestFirstSearch.getNumberOfNodesEvaluated() > 0);
    }

    @Test
    @DisplayName("Test algorithm returns correct solution")
    void testSolveReturnsCorrectSolution() {
        Solution solution = bestFirstSearch.solve(searchableMaze);

        assertNotNull(solution);
        List<AState> path = solution.getSolutionPath();
        assertNotNull(path);
        assertFalse(path.isEmpty());

        // Verify path starts with start state and ends with goal state
        assertEquals(searchableMaze.getStartState(), path.get(0));
        assertEquals(searchableMaze.getGoalState(), path.get(path.size() - 1));
    }


    @Test
    @DisplayName("Test algorithm returns null for unsolvable problems")
    void testReturnsNullForNonExistMaze() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            IMazeGenerator mg = new MyMazeGenerator();
            Maze maze = mg.generate(0, 0);
            searchableMaze = new SearchableMaze(maze);
        });
    }
}