package test;
import algorithms.search.*;
import algorithms.maze3D.*;

import java.util.ArrayList;

/**
 * A test class to run and evaluate different search algorithms on a 3D maze.
 * This class demonstrates how to solve a 3D maze using various searching algorithms
 * and compares their performance.
 */
public class RunSearchOnMaze3D {
    /**
     * Main method to execute search algorithm tests on a generated 3D maze.
     * Creates a 3D maze and solves it using BFS, DFS, and Best-First-Search algorithms.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        IMaze3DGenerator mg = new MyMaze3DGenerator();
        Maze3D maze = mg.generate(50, 50, 50);
        maze.print();
        SearchableMaze3D searchableMaze = new SearchableMaze3D(maze);
        solveProblem(searchableMaze, new BreadthFirstSearch());
        solveProblem(searchableMaze, new DepthFirstSearch());
        solveProblem(searchableMaze, new BestFirstSearch());
    }

    /**
     * Solves a searchable problem using the specified search algorithm.
     * Prints the algorithm name, number of nodes evaluated during the search process,
     * and the complete solution path.
     *
     * @param domain the searchable problem domain to solve (e.g., a 3D maze)
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
