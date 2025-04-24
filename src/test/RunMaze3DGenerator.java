package test;
import algorithms.maze3D.*;

/**
 * A test class to run and evaluate the 3D maze generator.
 * This class demonstrates the functionality of the 3D maze generation algorithm
 * and provides performance metrics.
 */
public class RunMaze3DGenerator {
    /**
     * Main method to execute the 3D maze generation test.
     * Creates and evaluates a maze using the MyMaze3DGenerator implementation.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        testMazeGenerator(new MyMaze3DGenerator());
    }

    /**
     * Tests a specified 3D maze generator algorithm.
     * Measures generation time, creates a sample maze, and prints relevant information
     * including the start and goal positions.
     *
     * @param mazeGenerator the 3D maze generator algorithm to test
     */
    private static void testMazeGenerator(IMazeGenerator3D mazeGenerator) {
// prints the time it takes the algorithm to run
        System.out.println(String.format("Maze generation time(ms): %s", mazeGenerator.measureAlgorithmTimeMillis(100/*rows*/,100/*columns*/,100)));
// generate another maze
        Maze3D maze = mazeGenerator.generate(50, 50,50);
// prints the maze
        if (maze != null) {
            maze.print();
// get the maze entrance
            Position3D startPosition = maze.getStartPosition();
// print the start position
            System.out.println(String.format("Start Position: %s", startPosition)); // format "{row,column}"
// prints the maze exit position
            System.out.println(String.format("Goal Position: %s", maze.getGoalPosition()));
        }
    }
}
