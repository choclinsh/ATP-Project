package test;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.*;
import java.util.ArrayList;

/**
 * A test class to run and evaluate different search algorithms on a 2D maze.
 * This class demonstrates how to solve a maze using various searching algorithms
 * and compares their performance.
 */
public class RunSearchOnMaze {
    /**
     * Main method to execute search algorithm tests on a generated maze.
     * Creates a maze and solves it using BFS, DFS, and Best-First-Search algorithms.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(100, 100); // will be null if inserted less than 2 as args
        if (maze != null) {
            maze.print();
            SearchableMaze searchableMaze = new SearchableMaze(maze);
            solveProblem(searchableMaze, new BreadthFirstSearch());
            solveProblem(searchableMaze, new DepthFirstSearch());
            solveProblem(searchableMaze, new BestFirstSearch());
        }
    }

    /**
     * Solves a searchable problem using the specified search algorithm.
     * Prints the algorithm name, number of nodes evaluated during the search process,
     * and the complete solution path.
     *
     * @param domain the searchable problem domain to solve (e.g., a maze)
     * @param searcher the search algorithm to use for solving the problem
     */
    private static void solveProblem(ISearchable domain, ISearchingAlgorithm
            searcher) {
//Solve a searching problem with a searcher
        try {
            Solution solution = searcher.solve(domain);
            System.out.println(String.format("'%s' algorithm - nodes evaluated: %s", searcher.getName(), searcher.getNumberOfNodesEvaluated()));
//Printing Solution Path
            System.out.println("Solution path:");
            ArrayList<AState> solutionPath = solution.getSolutionPath();
            for (int i = 0; i < solutionPath.size(); i++) {
                System.out.println(String.format("%s. %s", i, solutionPath.get(i)));
            }
        } catch (IllegalArgumentException e){
            System.out.println("Cant search on a null maze");
        }
    }
}
