package algorithms.mazeGenerators;
/**
 * Interface representing a maze generator algorithm.
 * Defines methods for generating mazes and measuring algorithm performance.
 */
public interface IMazeGenerator {
    /**
     * Generates a maze with the specified dimensions.
     *
     * @param rows The number of rows in the maze
     * @param cols The number of columns in the maze
     * @return A new Maze object
     */
    Maze generate(int rows, int cols);
    /**
     * Measures the time it takes to generate a maze with the specified dimensions.
     *
     * @param rows The number of rows in the maze
     * @param cols The number of columns in the maze
     * @return The time in milliseconds it took to generate the maze
     */
    long measureAlgorithmTimeMillis(int rows, int cols);
}
