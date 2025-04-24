package algorithms.search;

import static org.junit.jupiter.api.Assertions.*;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

/**
 * Test class for the BestFirstSearch algorithm implementation.
 * This class verifies the correctness and functionality of the BestFirstSearch algorithm
 * by testing it on maze problems of various sizes.
 */
class BestFirstSearchTest {
    private BestFirstSearch bestFirstSearch;
    private SearchableMaze searchableMaze;

    /**
     * Sets up the test environment before each test case.
     * Initializes a BestFirstSearch instance and creates a searchable maze
     * with dimensions 1000x1000 using the MyMazeGenerator.
     */
    @BeforeEach
    void setUp() {
        bestFirstSearch = new BestFirstSearch();
        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(1000, 1000);
        searchableMaze = new SearchableMaze(maze);
    }

    /**
     * Tests that the algorithm returns the correct name.
     * Verifies that getName() returns "Best first search".
     */
    @Test
    @DisplayName("Test algorithm name is correct")
    void testGetName() {
        assertEquals("Best first search", bestFirstSearch.getName());
    }

    /**
     * Tests that the evaluatedNodes counter increments correctly.
     * Verifies that the counter starts at 0 and increases after solving a maze.
     */
    @Test
    @DisplayName("Test evaluatedNodes counter increments correctly")
    void testEvaluatedNodesCounter() {
        assertEquals(0, bestFirstSearch.getNumberOfNodesEvaluated());
        bestFirstSearch.solve(searchableMaze);
        assertTrue(bestFirstSearch.getNumberOfNodesEvaluated() > 0);
    }

    /**
     * Tests that the algorithm returns the correct solution.
     * Verifies that the solution exists, is not empty, and starts with the start state
     * and ends with the goal state.
     */
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

    /**
     * Tests that the algorithm throws an exception when attempting to solve an
     * invalid maze (with dimensions 0x0).
     * Verifies that an IllegalArgumentException is thrown when creating a SearchableMaze
     * from a 0x0 maze.
     */
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