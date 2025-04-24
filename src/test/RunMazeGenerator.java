package test;
import algorithms.mazeGenerators.*;

/**
 * A test class to run and evaluate different 2D maze generators.
 * This class demonstrates the functionality of various maze generation algorithms
 * and provides performance metrics for comparison.
 */
public class RunMazeGenerator {
    /**
     * Main method to execute tests on different maze generators.
     * Tests three different implementations: Empty, Simple, and MyMazeGenerator.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        testMazeGenerator(new EmptyMazeGenerator());
        testMazeGenerator(new SimpleMazeGenerator());
        testMazeGenerator(new MyMazeGenerator());
    }

    /**
     * Tests a specified maze generator algorithm.
     * Measures generation time, creates a sample maze, and prints relevant information
     * including the start and goal positions.
     *
     * @param mazeGenerator the maze generator algorithm to test
     */
    private static void testMazeGenerator(IMazeGenerator mazeGenerator) {
// prints the time it takes the algorithm to run

        System.out.println(String.format("Maze generation time(ms): %s", mazeGenerator.measureAlgorithmTimeMillis(100/*rows*/,100/*columns*/)));
// generate another maze
        Maze maze = mazeGenerator.generate(100/*rows*/, 100/*columns*/);
// prints the maze
        if (maze != null) {
            maze.print();
// get the maze entrance
            Position startPosition = maze.getStartPosition();
// print the start position
            System.out.println(String.format("Start Position: %s", startPosition)); // format "{row,column}"
// prints the maze exit position
            System.out.println(String.format("Goal Position: %s", maze.getGoalPosition()));
        }
    }
}